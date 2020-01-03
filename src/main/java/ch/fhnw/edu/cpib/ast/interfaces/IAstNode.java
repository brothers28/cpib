package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypeIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAstNode {

    public abstract String toString(String indent);

    public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError;

    public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError;

    public void executeTypeCheck() throws TypeCheckingError, CastError;

    public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError,
            CannotAssignToConstError;

    public void setInit(TypeIdent ident);

    public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError;
}
