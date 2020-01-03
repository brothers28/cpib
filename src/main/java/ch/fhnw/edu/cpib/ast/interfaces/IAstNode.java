package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypedIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAstNode {

    String toString(String indent);

    void saveNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError;

    void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError;

    void executeTypeCheck() throws TypeCheckError, CastError;

    void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError, AssignToConstError;

    void setInit(TypedIdent ident);

    void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly) throws CodeTooSmallError;
}
