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
    private Operator monadicOpr;
    private IFactor factor;
    private Types castType;

    public MonadicFactor(Operator monadicOpr, IFactor factor) {
        this.monadicOpr = monadicOpr;
        this.factor = factor;
    }

    public MonadicFactor(Operator monadicOpr) {
        this.monadicOpr = monadicOpr;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        factor.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        factor.doScopeChecking();
    }

    @Override public void doTypeCasting(Types type) {
        if (type != null) {
            this.castType = type;
        }
        factor.doTypeCasting(type);
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

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        factor.doTypeChecking();

        // Check allowed types
        if (Terminals.NOTOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.BOOL)
            throw new TypeCheckingError(Types.BOOL, factor.getType());
        if (Terminals.ADDOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.INT64)
            throw new TypeCheckingError(Types.INT64, factor.getType());
        if (Terminals.ADDOPR.equals(monadicOpr.getOperator()) && factor.getType() != Types.NAT64)
            throw new TypeCheckingError(Types.NAT64, factor.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        factor.doInitChecking(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        // Add the value on top of stack
        factor.addInstructionToCodeArray(localLocations, simulateOnly);

        // Negate it
        if (!simulateOnly) {
            if (Terminals.NOTOPR.equals(monadicOpr.getOperator())) {
                codeArray.put(codeArrayPointer, new IInstructions.NegBool());
            } else if (Operators.MINUS.equals(monadicOpr.getOperator())) {
                if (Types.INT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.NegInt());
                } else if (Types.NAT64.equals(getType())) {
                    codeArray.put(codeArrayPointer, new IInstructions.NegNat());
                } else {
                    throw new RuntimeException("Unknown Type!");
                }
            } else {
                throw new RuntimeException("UNSUPPORTED MONADIC OPERATOR!");
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
        s += argumentIndent + "<monadicOpr>: " + monadicOpr.toString() + "\n";
        s += argumentIndent + "<factor>:\n";
        s += factor.toString(subIndent);

        return s;
    }

}
