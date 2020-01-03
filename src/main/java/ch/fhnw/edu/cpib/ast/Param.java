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
    private TypedIdent typedIdent;
    private LRValue lrValue;

    public Param(Flowmodes flowMode, Mechmodes mechMode, Changemodes changeMode, TypedIdent typedIdent) {
        this.flowMode = flowMode != null ? flowMode : Flowmodes.IN;
        this.mechMode = mechMode != null ? mechMode : Mechmodes.COPY;
        this.changeMode = changeMode != null ? changeMode : Changemodes.CONST;
        this.typedIdent = typedIdent;
        this.typedIdent.setInit();
        // Set the const boolean value on the typeIdent to true
        if (changeMode == Changemodes.CONST)
            this.typedIdent.setConst();
        lrValue = this.mechMode == Mechmodes.COPY ? LRValue.RVALUE : LRValue.LVALUE;
    }

    public String getIdentString() {
        return typedIdent.getValue();
    }

    public TypedIdent getTypedIdent() {
        return typedIdent;
    }

    public LRValue getLRValue() {
        return lrValue;
    }

    public Mechmodes getMechMode() {
        return mechMode;
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
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        //
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
        if (flowMode != null)
            s += argumentIndent + "<flowMode>: " + flowMode.toString() + "\n";
        if (mechMode != null)
            s += argumentIndent + "<mechMode>: " + mechMode.toString() + "\n";
        if (changeMode != null)
            s += argumentIndent + "<changeMode>: " + changeMode.toString() + "\n";
        s += argumentIndent + "<typeIdent>:\n";
        s += typedIdent.toString(subIndent);

        return s;
    }
}
