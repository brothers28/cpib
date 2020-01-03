package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypeIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAstNode {

    public abstract String toString(String indent);

    public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError;

    public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError;

    public void executeTypeCheck() throws TypeCheckError, CastError;

    public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError, AssignToConstError;

    public void setInit(TypeIdent ident);

    public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError;
}
