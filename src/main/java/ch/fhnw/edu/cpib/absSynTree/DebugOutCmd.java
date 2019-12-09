package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class DebugOutCmd extends AbsSynTreeNode implements ICmd {
	private IExpr expr;
	
	public DebugOutCmd(IExpr expr) {
		this.expr = expr;
	}
	
	@Override
	public void saveNamespaceInfoToNode(
			HashMap<String, TypeIdent> localStoresNamespace)
			throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
		this.localStoresNamespace = localStoresNamespace;
		expr.saveNamespaceInfoToNode(this.localStoresNamespace);
	}
	
	@Override
	public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
		expr.doScopeChecking();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		expr.doTypeChecking();
		// TODO: Check LRValue
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		expr.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {
		
		expr.addIInstrToCodeArray(localLocations, simulateOnly);
		
		String indicator;
		if(expr instanceof InitFactor) {
			indicator = ((InitFactor)expr).ident.getIdent();
		} else {
			indicator = "<anonymous>";
		}
		
		if(!simulateOnly) {
			if(expr.getType() == Types.BOOL) {
				codeArray.put(codeArrayPointer, new IInstructions.OutputBool(indicator));
			} else if (expr.getType() == Types.INT64) {
				codeArray.put(codeArrayPointer, new IInstructions.OutputInt(indicator));
			}
			//TODO....
			/*else if (expr.getType() == Types.NAT64) {
				codeArray.put(codeArrayPointer, new IInstructions.OutputNat(indicator));
			} */
			else {
				throw new RuntimeException("Unknown Type!");
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
		s += argumentIndent + "<expr>:\n";
		s += expr.toString(subIndent);
		
		return s;
	}	
}
