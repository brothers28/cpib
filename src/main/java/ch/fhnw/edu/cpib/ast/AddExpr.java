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
    protected Operators addOpr;
    protected IExpr exprLeft;
    protected IExpr exprRight;
    protected Types castType;

    public AddExpr(Operators addOpr, IExpr exprLeft, IExpr exprRight) {
        this.addOpr = addOpr;
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
        if (exprLeft.getType() == Types.BOOL)
            throw new TypeCheckError(Types.INT32, exprLeft.getType());
        if (exprLeft.getType() != exprRight.getType())
            throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
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
            switch (addOpr) {
            case PLUS:
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.AddInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.AddNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            case MINUS:
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.SubInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.SubNat());
                } else
                    throw new RuntimeException("Unknown Type!");
                break;
            }
        }
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        // Set horizontal spaces
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";

        // Get class
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndent + "<addOpr>: " + addOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
