package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypedIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAstNode {

    void setInit(TypedIdent ident);

    void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError;

    void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError;

    void executeTypeCheck() throws TypeCheckError, CastError;

    void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError;

    void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec) throws CodeTooSmallError;

    String toString(String spaces);

}
