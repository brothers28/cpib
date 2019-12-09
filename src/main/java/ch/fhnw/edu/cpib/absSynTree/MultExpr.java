package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class MultExpr extends AbsSynTreeNode implements IExpr {
	private Operators multOpr;
	private IExpr exprLeft;
	private IExpr exprRight;
	
	public MultExpr(Operators multOpr, IExpr exprLeft, IExpr exprRight) {
		this.multOpr = multOpr;
		this.exprLeft = exprLeft;
		this.exprRight = exprRight;
	}
	
	@Override
	public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		exprLeft.saveNamespaceInfoToNode(this.localStoresNamespace);
		exprRight.saveNamespaceInfoToNode(this.localStoresNamespace);
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
		exprLeft.doScopeChecking();
		exprRight.doScopeChecking();
	}
	
	@Override
	public LRValue getLRValue() {
		return LRValue.RVALUE;
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		exprLeft.doTypeChecking();
		exprRight.doTypeChecking();
		
		if(exprLeft.getType() == Types.BOOL)
			throw new TypeCheckError(Types.INT64, exprLeft.getType());
		if(exprLeft.getType() != exprRight.getType())
			throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
	}

	@Override
	public Types getType() {
		return exprLeft.getType();
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		exprLeft.doInitChecking(globalProtected);
		exprRight.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);
		exprRight.addIInstrToCodeArray(localLocations, simulateOnly);
		
		if(!simulateOnly) {
			switch(multOpr) {
				case TIMES:
					codeArray.put(codeArrayPointer, new IInstructions.MultInt());
					break;
				case DIV_E:
					codeArray.put(codeArrayPointer, new IInstructions.DivEuclInt());
					break;
				case MOD_E:
					codeArray.put(codeArrayPointer, new IInstructions.ModEuclInt());
					break;
			}
		}			
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
		s += argumentIndent + "<multOpr>: " + multOpr.toString() + "\n";
		s += argumentIndent + "<exprLeft>:\n";
		s += exprLeft.toString(subIndent);
		s += argumentIndent + "<exprRight>:\n";
		s += exprRight.toString(subIndent);
		
		return s;
	}	
	

}
