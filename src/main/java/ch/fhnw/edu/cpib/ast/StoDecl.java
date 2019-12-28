package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class StoDecl extends AstNode implements IDecl {
    private Changemodes changeMode;
    private TypeIdent typeIdent;

    public StoDecl(Changemodes changeMode, TypeIdent typeIdent) {
        this.changeMode = changeMode;
        this.typeIdent = typeIdent;
        // Set the const boolean value on the typeIdent to true
        if (changeMode == Changemodes.CONST)
            this.typeIdent.setConst();
    }

    public StoDecl(TypeIdent typeIdent) {
        this.typeIdent = typeIdent;
    }

    public TypeIdent getTypeIdent() {
        return typeIdent;
    }

    @Override public String getIdentString() {
        return typeIdent.getValue();
    }

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
    }

    @Override public void doScopeChecking() throws NotDeclaredError {
        // Do nothing
    }

    @Override public void doTypeChecking() throws TypeCheckError {
        // Do nothing
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // Do nothing
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        if (changeMode != null)
            s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
        s += argumentIndent + "<typeIdent>:\n";
        s += typeIdent.toString(subIndent);

        return s;
    }
}
