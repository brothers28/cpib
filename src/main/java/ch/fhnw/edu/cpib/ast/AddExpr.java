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

public class AddExpr extends AstNode implements IExpr {
    private Operators addOpr;
    private IExpr exprLeft;
    private IExpr exprRight;
    private Types castType;

    public AddExpr(Operators addOpr, IExpr exprLeft, IExpr exprRight) {
        this.addOpr = addOpr;
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.saveNamespaceInfo(this.localVarNamespace);
        exprRight.saveNamespaceInfo(this.localVarNamespace);

    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        exprLeft.doScopeChecking();
        exprRight.doScopeChecking();
    }

    @Override public void doTypeCasting(Types type) {
        if (type != null) {
            this.castType = type;
        }
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

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        exprLeft.doTypeChecking();
        exprRight.doTypeChecking();

        // Check allowed types
        if (exprLeft.getType() == Types.BOOL)
            throw new TypeCheckingError(Types.INT64, exprLeft.getType());
        if (exprLeft.getType() != exprRight.getType())
            throw new TypeCheckingError(exprLeft.getType(), exprRight.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        exprLeft.doInitChecking(globalProtected);
        exprRight.doInitChecking(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        exprLeft.addInstructionToCodeArray(localLocations, simulateOnly);
        exprRight.addInstructionToCodeArray(localLocations, simulateOnly);

        if (!simulateOnly) {
            switch (addOpr) {
            case PLUS:
                if (Types.INT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.AddInt());
                } else if (Types.NAT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.AddNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            case MINUS:
                if (Types.INT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.SubInt());
                } else if (Types.NAT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.SubNat());
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
        s += argumentIndent + "<addOpr>: " + addOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
