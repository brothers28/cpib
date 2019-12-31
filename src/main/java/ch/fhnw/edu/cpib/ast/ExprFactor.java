package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ExprFactor extends AstNode implements IFactor {
    private IExpr expr;
    private Types castType;

    public ExprFactor(IExpr expr) {
        this.expr = expr;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        expr.executeScopeCheck();
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
        expr.executeTypeCast(type);
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
        return expr.getType();
    }

    @Override public void executeTypeCheck() throws TypeCheckingError, CastError {
        expr.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        expr.executeInitCheck(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        // Add the value on top of stack
        expr.addInstructionToCodeArray(localLocations, simulateOnly);
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
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);

        return s;
    }
}
