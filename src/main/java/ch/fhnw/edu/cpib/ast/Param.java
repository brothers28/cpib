package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Param extends AstNode {
    private Flowmodes flowMode;
    private Mechmodes mechMode;
    private Changemodes changeMode;
    private TypeIdent typeIdent;
    private LRValue lrValue;

    public Param(Flowmodes flowMode, Mechmodes mechMode, Changemodes changeMode, TypeIdent typeIdent) {
        this.flowMode = flowMode != null ? flowMode : Flowmodes.IN;
        this.mechMode = mechMode != null ? mechMode : Mechmodes.COPY;
        this.changeMode = changeMode != null ? changeMode : Changemodes.CONST;
        this.typeIdent = typeIdent;
        this.typeIdent.setInit();
        // Set the const boolean value on the typeIdent to true
        if (changeMode == Changemodes.CONST)
            this.typeIdent.setConst();
        lrValue = this.mechMode == Mechmodes.COPY ? LRValue.RVALUE : LRValue.LVALUE;
    }

    public String getIdentString() {
        return typeIdent.getValue();
    }

    public TypeIdent getTypeIdent() {
        return typeIdent;
    }

    public LRValue getLRValue() {
        return lrValue;
    }

    public Mechmodes getMechMode() {
        return mechMode;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void doScopeChecking() throws NotDeclaredError {
        //
    }

    @Override public void doTypeChecking() throws TypeCheckingError {
        //
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        //
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
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        if (flowMode != null)
            s += argumentIndent + "<flowMode>: " + flowMode.toString() + "\n";
        if (mechMode != null)
            s += argumentIndent + "<mechMode>: " + mechMode.toString() + "\n";
        if (changeMode != null)
            s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
        s += argumentIndent + "<typeIdent>:\n";
        s += typeIdent.toString(subIndent);

        return s;
    }
}
