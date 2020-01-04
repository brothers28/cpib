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
    protected Operators boolOpr;
    protected IExpr exprLeft;
    protected IExpr exprRight;
    protected Types castType;

    public BoolExpr(Operators boolOpr, IExpr exprLeft, IExpr exprRight) {
        this.boolOpr = boolOpr;
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
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
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

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        exprLeft.addToCodeArray(localLocations, noExec);
        exprRight.addToCodeArray(localLocations, noExec);

        // Add instruction depending on (casted) type
        if (!noExec) {
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
        s += argumentIndendation + "<boolOpr>: " + boolOpr.toString() + "\n";
        s += argumentIndendation + "<exprLeft>:\n";
        s += exprLeft.toString(lowerSpaces);
        s += argumentIndendation + "<exprRight>:\n";
        s += exprRight.toString(lowerSpaces);

        return s;
    }
}
