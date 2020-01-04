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
    protected Flowmodes flowMode;
    protected Mechmodes mechMode;
    protected Changemodes changeMode;
    protected TypedIdent typedIdent;
    protected LRValue lrValue;

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
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        //
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
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

        // Add elements
        if (flowMode != null)
            s += argumentIndendation + "<flowMode>: " + flowMode.toString() + "\n";
        if (mechMode != null)
            s += argumentIndendation + "<mechMode>: " + mechMode.toString() + "\n";
        if (changeMode != null)
            s += argumentIndendation + "<changeMode>: " + changeMode.toString() + "\n";

        s += argumentIndendation + "<typeIdent>:\n";
        s += typedIdent.toString(lowerSpaces);

        return s;
    }
}
