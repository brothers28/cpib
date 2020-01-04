package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Program extends AstNode {
    protected Ident ident;
    protected ArrayList<IDecl> globalDeclarations;
    protected CpsCmd cpsCmd;

    public Program(Ident ident, ArrayList<IDecl> globalDeclarations, CpsCmd cpsCmd) {
        this.ident = ident;
        this.globalDeclarations = globalDeclarations;
        this.cpsCmd = cpsCmd;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        // Check namespaces
        for (IDecl decl : globalDeclarations) {
            if (decl instanceof StoDecl) {
                if (AstNode.globalVarNamespace.containsKey(decl.getIdentString())) {
                    // Already declared
                    throw new AlreadyDeclaredError(decl.getIdentString());
                }

                // Save to global namespace
                AstNode.globalVarNamespace.put(decl.getIdentString(), ((StoDecl) decl).getTypedIdent());
            } else {
                if (AstNode.globalRoutNamespace.containsKey(decl.getIdentString())) {
                    // Already declared
                    throw new AlreadyDeclaredError(decl.getIdentString());
                }

                // Save to global namespace
                AstNode.globalRoutNamespace.put(decl.getIdentString(), decl);
            }
        }

        for (IDecl decl : globalDeclarations) {
            // For funDecl and procDecl, store the local variables into the nodes and child nodes
            if (!(decl instanceof StoDecl))
                decl.setNamespaceInfo(new HashMap<String, TypedIdent>());
        }
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        for (IDecl decl : globalDeclarations) {
            if (!(decl instanceof StoDecl))
                decl.executeScopeCheck();
        }

        cpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        for (IDecl decl : globalDeclarations) {
            if (!(decl instanceof StoDecl))
                decl.executeTypeCheck();
        }

        cpsCmd.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        cpsCmd.executeInitCheck(globalProtected);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        // Add addresses of global declarations to global address map
        for (IDecl decl : globalDeclarations) {
            if (decl instanceof StoDecl) {
                globalVarAdresses.put(decl.getIdentString(), codeArrayPointer);
                decl.addToCodeArray(localLocations, noExec);
            }
        }

        // Save codeArrayPointer
        int pointerBefore = codeArrayPointer;
        // Try to fill stack
        // NoExec = true
        for (IDecl decl : globalDeclarations) {
            if (!(decl instanceof StoDecl)) {
                globalRoutAdresses.put(decl.getIdentString(), codeArrayPointer + 1); // Overjump conditional jump
                decl.addToCodeArray(localLocations, true);
            }
        }

        int addressAfterDeclaration = codeArrayPointer + 1; // Overjump conditional jump
        // Restore pointer
        codeArrayPointer = pointerBefore;

        // Jump to programm start
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.UncondJump(addressAfterDeclaration));
        codeArrayPointer++;

        // Exec
        for (IDecl decl : globalDeclarations) {
            if (!(decl instanceof StoDecl)) {
                decl.addToCodeArray(localLocations, noExec);
            }
        }
        cpsCmd.addToCodeArray(localLocations, noExec);

        // End of programm
        codeArray.put(codeArrayPointer, new IInstructions.Stop());
        codeArrayPointer++;
    }

    @Override public String toString(String spaces) {
        // Set horizontal spaces
        String identifierIndendation = spaces;
        String argumentIndendation = spaces + " ";
        String lowerSpaces = spaces + "  ";

        // Get class
        String s = "";
        s += identifierIndendation + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndendation + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        if (AstNode.globalVarNamespace != null)
            s += argumentIndendation + "[globalStoresNamespace]: " + globalVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        if (AstNode.globalRoutNamespace != null)
            s += argumentIndendation + "[globalRoutinesNamespace]: " + globalRoutNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndendation + "<ident>: " + ident.toString() + "\n";
        s += argumentIndendation + "<globalDeclarations>:\n";
        for (IDecl decl : globalDeclarations) {
            s += decl.toString(lowerSpaces);
        }
        s += argumentIndendation + "<cpsCmd>:";
        s += cpsCmd.toString(lowerSpaces);

        return s;
    }
}
