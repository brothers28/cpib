package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class StoDecl extends AstNode implements IDecl {
    protected Changemodes changeMode;
    protected TypedIdent typedIdent;

    public StoDecl(Changemodes changeMode, TypedIdent typedIdent) {
        this.changeMode = changeMode;
        this.typedIdent = typedIdent;
        // Set the const boolean value on the typeIdent to true
        if (changeMode == Changemodes.CONST)
            this.typedIdent.setConst();
    }

    public StoDecl(TypedIdent typedIdent) {
        this.typedIdent = typedIdent;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void executeScopeCheck() throws NotDeclaredError {
        //
    }

    @Override public void executeTypeCheck() throws TypeCheckError {
        //
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        //
    }

    public TypedIdent getTypedIdent() {
        return typedIdent;
    }

    @Override public String getIdentString() {
        return typedIdent.getValue();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        if (changeMode != null)
            s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
        s += argumentIndent + "<typeIdent>:\n";
        s += typedIdent.toString(subIndent);

        return s;
    }
}
