package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.ast.TypeIdent;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public interface IAbsSynTreeNode {

	public abstract String toString(String indent);

	public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace) throws NameAlreadyDeclaredError,
			NameAlreadyGloballyDeclaredError, AlreadyInitializedError;

	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError;

	public void doTypeChecking() throws TypeCheckError, CastError;

	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError,
			GlobalInitializationProhibitedError, CannotAssignToConstError;

	public void setInit(TypeIdent ident);
	
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly) throws CodeTooSmallError;
}
