package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ExprFactor extends AbsSynTreeNode implements IFactor {
	private IExpr expr;
	private Types castType;

	public ExprFactor(IExpr expr) {
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

	@Override public void doTypeCasting(Types type) {
		if (type != null){
			this.castType = type;
		}
		expr.doTypeCasting(type);
	}

	@Override
	public LRValue getLRValue() {
		return LRValue.RVALUE;
	}

	@Override
	public Types getType() {
		if (castType != null){
			// type is casted
			return castType;
		}
		// otherwise get real type
		return expr.getType();
	}

	@Override
	public void doTypeChecking() throws TypeCheckError, CastError {
		expr.doTypeChecking();
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError, CannotAssignToConstError {
		expr.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {

		// Add the value on top of stack
		expr.addIInstrToCodeArray(localLocations, simulateOnly);
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
