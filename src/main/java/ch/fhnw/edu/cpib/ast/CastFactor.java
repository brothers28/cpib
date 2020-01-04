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
    protected Types castType;
    protected IFactor factor;

    public CastFactor(Types castType, IFactor factor) {
        this.castType = castType;
        this.factor = factor;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        factor.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        factor.executeScopeCheck();
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
        // Change unerlying factors
        factor.executeTypeCast(castType);
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        return castType;
    }

    @Override public void executeTypeCheck() throws CastError, TypeCheckError {
        factor.executeTypeCheck();

        // Check if casteable
        if (!isCastable(factor.getType(), getType()))
            throw new CastError(getType(), factor.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        factor.executeInitCheck(globalProtected);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        // Cast factor
        factor.executeTypeCast(castType);

        // Add to top of stack
        factor.addToCodeArray(localLocations, noExec);
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
        s += argumentIndent + "<CastType>: " + castType.toString() + "\n";
        s += argumentIndent + "<factor>:\n";
        s += factor.toString(subIndent);

        return s;
    }

}
