package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class MultExpr extends AstNode implements IExpr {
    protected Operators multOpr;
    protected IExpr exprLeft;
    protected IExpr exprRight;
    protected Types castType;

    public MultExpr(Operators multOpr, IExpr exprLeft, IExpr exprRight) {
        this.multOpr = multOpr;
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.setNamespaceInfo(this.localVarNamespace);
        exprRight.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        exprLeft.executeScopeCheck();
        exprRight.executeScopeCheck();
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        exprLeft.executeInitCheck(globalProtected);
        exprRight.executeInitCheck(globalProtected);
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        exprLeft.executeTypeCheck();
        exprRight.executeTypeCheck();

        // Check allowed types
        if (exprLeft.getType() == Types.BOOL)
            throw new TypeCheckError(Types.INT32, exprLeft.getType());
        if (exprLeft.getType() != exprRight.getType())
            throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        return exprLeft.getType();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        exprLeft.addToCodeArray(localLocations, noExec);
        exprRight.addToCodeArray(localLocations, noExec);

        // Add instruction depending on (casted) type
        if (!noExec) {
            switch (multOpr) {
            case TIMES:
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.MultInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.MultNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            case DIV_E:
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.DivEuclInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.DivEuclNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            case MOD_E:
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.ModEuclInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.ModEuclNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            }
        }
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
        s += argumentIndendation + "<multOpr>: " + multOpr.toString() + "\n";
        s += argumentIndendation + "<exprLeft>:\n";
        s += exprLeft.toString(lowerSpaces);
        s += argumentIndendation + "<exprRight>:\n";
        s += exprRight.toString(lowerSpaces);

        return s;
    }

}
