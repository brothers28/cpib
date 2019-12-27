package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CpsCmd extends AbsSynTreeNode implements ICmd {
	private ArrayList<ICmd> commands;

	public CpsCmd(ArrayList<ICmd> commands) {
		this.commands = commands;
	}
	
	@Override
	public void saveNamespaceInfoToNode(
			HashMap<String, TypeIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		for(ICmd command : commands) {
			command.saveNamespaceInfoToNode(this.localStoresNamespace);
		}		
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
		for(ICmd command : commands) {
			command.doScopeChecking();
		}
	}

	@Override
	public void doTypeChecking() throws TypeCheckError, CastError {
		for(ICmd command : commands) {
			command.doTypeChecking();
		}
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		for(ICmd command : commands) {
			command.doInitChecking(globalProtected);
		}
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		for(ICmd command : commands) {
			command.addIInstrToCodeArray(localLocations, simulateOnly);
		}		
	}

	@Override
	public String toString(String indent) {
		String nameIndent = indent;
		String argumentIndent = indent + " ";
		String subIndent = indent + "  ";
		String s = "";
		s += nameIndent + this.getClass().getName() + "\n";
		if(localStoresNamespace != null)
			s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream().map(Object::toString).collect(Collectors.joining(",")) + "\n";		
		s += argumentIndent + "<commands>:\n";
		for(ICmd cmd : commands) {
			s += cmd.toString(subIndent);
		}
		
		return s;
	}	
}
