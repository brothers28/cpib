package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

import static ch.fhnw.edu.cpib.parser.util.CastChecker.isCastable;

public class CastFactor extends AstNode implements IFactor {
    private Types castType;
    private IFactor factor;

    public CastFactor(Types castType, IFactor factor) {
        this.castType = castType;
        this.factor = factor;
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
        // Change unerlying factors
        factor.doTypeCasting(castType);
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        return castType;
    }

    @Override public void doTypeChecking() throws CastError, TypeCheckingError {
        factor.doTypeChecking();

        // Check if casteable
        if (!isCastable(factor.getType(), getType()))
            throw new CastError(getType(), factor.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        factor.doInitChecking(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        // Cast factor
        factor.doTypeCasting(castType);

        // Add the value on top of stack
        factor.addInstructionToCodeArray(localLocations, simulateOnly);

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
        s += argumentIndent + "<CastType>: " + castType.toString() + "\n";
        s += argumentIndent + "<factor>:\n";
        s += factor.toString(subIndent);

        return s;
    }

}
