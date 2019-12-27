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

public class RelExpr extends AbsSynTreeNode implements IExpr {
	private Operators relOpr;
	private IExpr exprLeft;
	private IExpr exprRight;
	private Types castType;
	
	public RelExpr(Operators relOpr, IExpr exprLeft, IExpr exprRight) {
		this.relOpr = relOpr;
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

	@Override public void doTypeCasting(Types type) {
		if (type != null){
			this.castType = type;
		}
		exprLeft.doTypeCasting(type);
		exprRight.doTypeCasting(type);
	}
	
	@Override
	public LRValue getLRValue() {
		return LRValue.RVALUE;
	}

	@Override
	public void doTypeChecking() throws TypeCheckError {
		if (exprLeft instanceof ch.fhnw.edu.cpib.absSynTree.RelExpr) {
			((RelExpr) exprLeft).exprLeft.doTypeChecking();
			((RelExpr) exprLeft).exprRight.doTypeChecking();
		} else {
			if (exprLeft.getType() == Types.BOOL)
				throw new TypeCheckError(Types.INT64, exprLeft.getType());
			if (exprLeft.getType() != exprRight.getType())
				throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
		}
	}

	@Override
	public Types getType() {
		if (castType != null){
			// type is casted
			return castType;
		}
		// otherwise get real type
		return Types.BOOL;
	}

	@Override
	public void doInitChecking(boolean globalProtected) throws NotInitializedError, AlreadyInitializedError,
			GlobalInitializationProhibitedError, CannotAssignToConstError {
		exprLeft.doInitChecking(globalProtected);
		exprRight.doInitChecking(globalProtected);
	}

	@Override
	public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
			throws CodeTooSmallError {

		if (exprLeft instanceof RelExpr) {
			int codeArrayPointerBefore = codeArrayPointer;
			RelExpr temp = new RelExpr(relOpr, ((RelExpr) exprLeft).exprRight, exprRight);
			temp.addIInstrToCodeArray(localLocations, true);
			int exprRightSize = codeArrayPointer - codeArrayPointerBefore + 1;
			codeArrayPointer = codeArrayPointerBefore;

			exprLeft.addIInstrToCodeArray(localLocations, true);
			int exprLeftSize = codeArrayPointer - codeArrayPointerBefore;
			codeArrayPointer = codeArrayPointerBefore;

			exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);
			if (!simulateOnly) {
				codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + exprRightSize));
			}
			codeArrayPointer++;

			temp.addIInstrToCodeArray(localLocations, simulateOnly);
			if (!simulateOnly) {
				codeArray.put(codeArrayPointer, new IInstructions.UncondJump(codeArrayPointer + 1 + exprLeftSize));
			}
			codeArrayPointer++;

			exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);

		} else {
			exprLeft.addIInstrToCodeArray(localLocations, simulateOnly);
			exprRight.addIInstrToCodeArray(localLocations, simulateOnly);

			if (!simulateOnly) {
				switch (relOpr) {
					case EQ:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.EqInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.EqNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
					case GE:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.GeInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.GeNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
					case GT:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.GtInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.GtNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
					case LE:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.LeInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.LeNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
					case LT:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.LtInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.LtNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
					case NE:
						if (Types.INT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.NegInt());
						} else if (Types.NAT64.equals(getType())) {
							codeArray.put(codeArrayPointer, new IInstructions.NeNat());
						} else
							throw new RuntimeException("Unknown Type!");
						break;
				}
			}
			codeArrayPointer++;
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
		s += argumentIndent + "<relOpr>: " + relOpr.toString() + "\n";
		s += argumentIndent + "<exprLeft>:\n";
		s += exprLeft.toString(subIndent);
		s += argumentIndent + "<exprRight>:\n";
		s += exprRight.toString(subIndent);
		
		return s;
	}	
}
