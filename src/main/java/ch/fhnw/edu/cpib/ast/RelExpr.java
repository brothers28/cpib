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

public class RelExpr extends AstNode implements IExpr {
    private Operators relOpr;
    private IExpr exprLeft;
    private IExpr exprRight;
    private Types castType;

    public RelExpr(Operators relOpr, IExpr exprLeft, IExpr exprRight) {
        this.relOpr = relOpr;
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

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        // Check allowed types
        if (exprLeft instanceof ch.fhnw.edu.cpib.ast.RelExpr) {
            ((RelExpr) exprLeft).exprLeft.executeTypeCheck();
            ((RelExpr) exprLeft).exprRight.executeTypeCheck();
        } else {
            if (exprLeft.getType() == Types.BOOL)
                throw new TypeCheckError(Types.INT32, exprLeft.getType());
            if (exprLeft.getType() != exprRight.getType())
                throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        exprLeft.executeInitCheck(globalProtected);
        exprRight.executeInitCheck(globalProtected);
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
        return Types.BOOL;
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {

        if (exprLeft instanceof RelExpr) {
            int pointerBefore = codeArrayPointer;
            RelExpr temp = new RelExpr(relOpr, ((RelExpr) exprLeft).exprRight, exprRight);
            // NoExec = true
            temp.addToCodeArray(localLocations, true);
            int exprRightSize = codeArrayPointer - pointerBefore + 1;
            codeArrayPointer = pointerBefore;

            // NoExec = true
            exprLeft.addToCodeArray(localLocations, true);
            int exprLeftSize = codeArrayPointer - pointerBefore;
            codeArrayPointer = pointerBefore;

            exprLeft.addToCodeArray(localLocations, noExec);
            // Exec
            if (!noExec) {
                codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + exprRightSize));
            }
            codeArrayPointer++;

            temp.addToCodeArray(localLocations, noExec);
            if (!noExec) {
                codeArray.put(codeArrayPointer, new IInstructions.UncondJump(codeArrayPointer + 1 + exprLeftSize));
            }
            codeArrayPointer++;

            exprLeft.addToCodeArray(localLocations, noExec);

        } else {
            exprLeft.addToCodeArray(localLocations, noExec);
            exprRight.addToCodeArray(localLocations, noExec);

            // Add instruction depending on (casted) type
            if (!noExec) {
                switch (relOpr) {
                case EQ:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.EqInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.EqNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                case GE:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.GeInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.GeNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                case GT:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.GtInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.GtNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                case LE:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.LeInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.LeNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                case LT:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.LtInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.LtNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                case NE:
                    if (Types.INT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.NegInt());
                    } else if (Types.NAT32.equals(exprLeft.getType())) {
                        codeArray.put(codeArrayPointer, new IInstructions.NeNat());
                    } else
                        throw new RuntimeException("Unknown Type!");
                    break;
                }
            }
            codeArrayPointer++;
        }
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
        s += argumentIndent + "<relOpr>: " + relOpr.toString() + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
