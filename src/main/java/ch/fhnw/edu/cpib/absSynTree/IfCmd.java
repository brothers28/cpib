package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.parser.DeepCopy;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;
import java.util.HashMap;
import java.util.stream.Collectors;

public class IfCmd extends AbsSynTreeNode implements ICmd {
	private IExpr expr;
	private CpsCmd ifCpsCmd;
	private CpsCmd elseCpsCmd;
	
	public IfCmd(IExpr expr, CpsCmd ifCpsCmd, CpsCmd elseCpsCmd) {
		this.expr = expr;
		this.ifCpsCmd = ifCpsCmd;
		this.elseCpsCmd = elseCpsCmd;
	}
	
	@Override
	public void saveNamespaceInfoToNode(
			HashMap<String, TypeIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		expr.saveNamespaceInfoToNode(this.localStoresNamespace);
		ifCpsCmd.saveNamespaceInfoToNode(DeepCopy.deepCopy(this.localStoresNamespace));
		elseCpsCmd.saveNamespaceInfoToNode(DeepCopy.deepCopy(this.localStoresNamespace));
	}

	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
		expr.doScopeChecking();
		ifCpsCmd.doScopeChecking();
		elseCpsCmd.doScopeChecking();
	}
	
	@Override
	public void doTypeChecking() throws TypeCheckError {
		expr.doTypeChecking();
		ifCpsCmd.doTypeChecking();
		elseCpsCmd.doTypeChecking();
		
		if(expr.getType() != Types.BOOL)
			throw new TypeCheckError(Types.BOOL, expr.getType());
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		expr.doInitChecking(globalProtected);
		// set recursively all initialized variables also on the child-nodes to init
		for(TypeIdent ident : localStoresNamespace.values()) {
			if(ident.getInit()) {
				ifCpsCmd.setInit(ident);
				elseCpsCmd.setInit(ident);
			}
		}
		// Do the init checking
		// Global variables cannot be initialized from now on
		ifCpsCmd.doInitChecking(true);	
		elseCpsCmd.doInitChecking(true);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		// get the size of ifCpsCmd by simulating the add action
		int codeArrayPointerBefore = codeArrayPointer;
		
		ifCpsCmd.addIInstrToCodeArray(localLocations, true);
		int ifCpsCmdSize = codeArrayPointer - codeArrayPointerBefore + 1; // + 1 for unconditional jump after exprFalse

		// reset pointer
		codeArrayPointer = codeArrayPointerBefore;
		
		// get the size of elseCpsCmd
		elseCpsCmd.addIInstrToCodeArray(localLocations, true);
		int elseCpsCmdSize = codeArrayPointer - codeArrayPointerBefore;
		
		// reset pointer
		codeArrayPointer = codeArrayPointerBefore;
		
		// now really add the staff
		// add the boolean for the conditional check onto the stack
		expr.addIInstrToCodeArray(localLocations, simulateOnly);
		// now add the jump condition to see if we had to continue (true part) or to jump (false part)
		if(!simulateOnly)
			codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + ifCpsCmdSize));
		codeArrayPointer++;
		// now add the true part
		ifCpsCmd.addIInstrToCodeArray(localLocations, simulateOnly);
		// now add the unconditional jump to jump after the false part (we already processed the true part ...)
		if(!simulateOnly)
			codeArray.put(codeArrayPointer, new IInstructions.UncondJump(codeArrayPointer + 1 + elseCpsCmdSize));
		codeArrayPointer++;
		// now add the false part		
		elseCpsCmd.addIInstrToCodeArray(localLocations, simulateOnly);
		
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
		s += argumentIndent + "<expr>:\n";
		s += expr.toString(subIndent);
		s += argumentIndent + "<ifCpsCmd>:\n";
		s += ifCpsCmd.toString(subIndent);
		s += argumentIndent + "<elseCpsCmd>:\n";
		s += elseCpsCmd.toString(subIndent);
		
		return s;
	}	
}
