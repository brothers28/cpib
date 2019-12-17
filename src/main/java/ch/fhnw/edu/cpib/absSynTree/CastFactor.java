package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class CastFactor extends AbsSynTreeNode implements IFactor {
	private Types castType;
	private IFactor factor;

	public CastFactor(Types castType, IFactor factor) {
		this.castType = castType;
		this.factor = factor;
	}

	public CastFactor(Types castType) {
		this.castType = castType;
	}
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		factor.saveNamespaceInfoToNode(this.localStoresNamespace);
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
		factor.doScopeChecking();
	}
	
	@Override
	public LRValue getLRValue() {
		return LRValue.RVALUE;
	}

	@Override
	public Types getType() {
		return factor.getType();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		factor.doTypeChecking();
		
		// TODO: Scope Checking
		/*
		if(Terminals.NOTOPR.equals(castType.getOperator()) && factor.getType() != Types.BOOL)
			throw new TypeCheckError(Types.BOOL, factor.getType());
		if(Terminals.ADDOPR.equals(castType.getOperator()) && factor.getType() != Types.INT64)
			throw new TypeCheckError(Types.INT64, factor.getType());
		if(Terminals.ADDOPR.equals(castType.getOperator()) && factor.getType() != Types.NAT64)
			throw new TypeCheckError(Types.NAT64, factor.getType());
		 */
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		factor.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		
		// Add the value on top of stack
		factor.addIInstrToCodeArray(localLocations, simulateOnly);
		
		// Negate it
		// TODO: Make running
		/*
		if(!simulateOnly) {
			if(Terminals.NOTOPR.equals(castType.getOperator())) {
				codeArray.put(codeArrayPointer, new IInstructions.NegBool());
			} else if(Operators.MINUS.equals(castType.getOperator())) {
				codeArray.put(codeArrayPointer, new IInstructions.NegInt());		
			} else {
				throw new RuntimeException("UNSUPPORTED CAST OPERATOR!");
			}
		} */
		codeArrayPointer++;	
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
		s += argumentIndent + "<CastType>: " + castType.toString() + "\n";
		s += argumentIndent + "<factor>:\n";
		s += factor.toString(subIndent);
		
		return s;
	}	
}