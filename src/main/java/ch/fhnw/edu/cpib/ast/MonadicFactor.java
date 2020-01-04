package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class MonadicFactor extends AstNode implements IFactor {
    protected Operator monadicOpr;
    protected IFactor factor;
    protected Types castType;

    public MonadicFactor(Operator monadicOpr, IFactor factor) {
        this.monadicOpr = monadicOpr;
        this.factor = factor;
    }

    public MonadicFactor(Operator monadicOpr) {
        this.monadicOpr = monadicOpr;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        factor.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        factor.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        factor.executeTypeCheck();

        // Check allowed types
        if (Terminals.NOTOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.BOOL)
            throw new TypeCheckError(Types.BOOL, factor.getType());
        if (Terminals.ADDOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.INT32)
            throw new TypeCheckError(Types.INT32, factor.getType());
        if (Terminals.ADDOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.NAT32)
            throw new TypeCheckError(Types.NAT32, factor.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        factor.executeInitCheck(globalProtected);
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
        factor.executeTypeCast(type);
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
        return factor.getType();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {

        // Add to top of stack
        factor.addToCodeArray(localLocations, noExec);

        // Negate
        if (!noExec) {
            if (Terminals.NOTOPR.equals(monadicOpr.getOperator())) {
                codeArray.put(codeArrayPointer, new IInstructions.NegBool());
            } else if (Operators.MINUS.equals(monadicOpr.getOperator())) {
                if (Types.INT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.NegInt());
                } else if (Types.NAT32.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.NegNat());
                } else {
                    throw new RuntimeException("Unknown Type!");
                }
            } else {
                throw new RuntimeException("Unknown monadic operator!");
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
        s += argumentIndent + "<monadicOpr>: " + monadicOpr.toString() + "\n";
        s += argumentIndent + "<factor>:\n";
        s += factor.toString(subIndent);

        return s;
    }

}
