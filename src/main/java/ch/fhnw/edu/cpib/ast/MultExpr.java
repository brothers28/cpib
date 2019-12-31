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
    private Operators multOpr;
    private IExpr exprLeft;
    private IExpr exprRight;
    private Types castType;

    public MultExpr(Operators multOpr, IExpr exprLeft, IExpr exprRight) {
        this.multOpr = multOpr;
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.saveNamespaceInfo(this.localVarNamespace);
        exprRight.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        exprLeft.executeScopeCheck();
        exprRight.executeScopeCheck();
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public void executeTypeCheck() throws TypeCheckingError, CastError {
        exprLeft.executeTypeCheck();
        exprRight.executeTypeCheck();

        // Check allowed types
        if (exprLeft.getType() == Types.BOOL)
            throw new TypeCheckingError(Types.INT32, exprLeft.getType());
        if (exprLeft.getType() != exprRight.getType())
            throw new TypeCheckingError(exprLeft.getType(), exprRight.getType());
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        return exprLeft.getType();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        exprLeft.executeInitCheck(globalProtected);
        exprRight.executeInitCheck(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        exprLeft.addInstructionToCodeArray(localLocations, simulateOnly);
        exprRight.addInstructionToCodeArray(localLocations, simulateOnly);

        if (!simulateOnly) {
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

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<multOpr>: " + multOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }

}
