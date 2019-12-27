package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

import static ch.fhnw.edu.cpib.parser.util.CastChecker.isCastable;

public class CastFactor extends AbsSynTreeNode implements IFactor {
	private Types castType;
	private IFactor factor;

	public CastFactor(Types castType, IFactor factor) {
		this.castType = castType;
		this.factor = factor;
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

	@Override public void doTypeCasting(Types type) {
		if (type != null){
			this.castType = type;
		}
		// Change unerlying factors
		factor.doTypeCasting(castType);
	}
	
	@Override
	public LRValue getLRValue() {
		return LRValue.RVALUE;
	}

	@Override
	public Types getType() {
		return castType;
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		factor.doTypeChecking();
		
		// TODO: Type Checking
		if(!isCastable(factor.getType(), getType()))
			throw new TypeCheckError(getType(), factor.getType());
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
		/*
		// FIXME: MAke run
		if(!simulateOnly) {
			if(Terminals.NOTOPR.equals(factor.getOperator())) {
				codeArray.put(codeArrayPointer, new IInstructions.NegBool());
			} else if(Operators.MINUS.equals(castType.getOperator())) {
				codeArray.put(codeArrayPointer, new IInstructions.NegInt());		
			} else {
				throw new RuntimeException("UNSUPPORTED CAST OPERATOR!");
			}
		}
		codeArrayPointer++;
		*/

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
