package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypeIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAstNode {

    String toString(String indent);

    void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError;

    void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError;

    void executeTypeCheck() throws TypeCheckError, CastError;

    void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError, AssignToConstError;

    void setInit(TypeIdent ident);

    void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly) throws CodeTooSmallError;
}
