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

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.setNamespaceInfo(this.localVarNamespace);
        exprRight.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        exprLeft.executeScopeCheck();
        exprRight.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        exprLeft.executeTypeCheck();
        exprRight.executeTypeCheck();

        // Check allowed types
        if (exprLeft.getType() != Types.BOOL)
            throw new TypeCheckError(Types.BOOL, exprLeft.getType());
        if (exprRight.getType() != Types.BOOL)
            throw new TypeCheckError(Types.BOOL, exprRight.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        exprLeft.executeInitCheck(globalProtected);
        exprRight.executeInitCheck(globalProtected);
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        if (castType != null) {
            // Type is casted
            return castType;
        }
        // Otherwise get real type
        return Types.BOOL;
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        exprLeft.addToCodeArray(localLocations, simulateOnly);
        exprRight.addToCodeArray(localLocations, simulateOnly);

        // Add instruction depending on (casted) type
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
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<boolOpr>: " + boolOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
