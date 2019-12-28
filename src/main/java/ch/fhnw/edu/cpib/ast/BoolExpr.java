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

public class BoolExpr extends AstNode implements IExpr {
    private Operators boolOpr;
    private IExpr exprLeft;
    private IExpr exprRight;
    private Types castType;

    public BoolExpr(Operators boolOpr, IExpr exprLeft, IExpr exprRight) {
        this.boolOpr = boolOpr;
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

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        exprLeft.doTypeChecking();
        exprRight.doTypeChecking();

        // Check allowed types
        if (exprLeft.getType() != Types.BOOL)
            throw new TypeCheckingError(Types.BOOL, exprLeft.getType());
        if (exprRight.getType() != Types.BOOL)
            throw new TypeCheckingError(Types.BOOL, exprRight.getType());
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        return Types.BOOL;
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        exprLeft.doInitChecking(globalProtected);
        exprRight.doInitChecking(globalProtected);
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);
        exprRight.addIInstrToCodeArray(localLocations, simulateOnly);

        if (!simulateOnly) {
            switch (boolOpr) {
            case AND:
                codeArray.put(codeArrayPointer, new IInstructions.AndBool());
                break;
            case OR:
                codeArray.put(codeArrayPointer, new IInstructions.OrBool());
                break;
            case CAND:
                codeArray.put(codeArrayPointer, new IInstructions.CAndBool());
                break;
            case COR:
                codeArray.put(codeArrayPointer, new IInstructions.COrBool());
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
        s += argumentIndent + "<boolOpr>: " + boolOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
