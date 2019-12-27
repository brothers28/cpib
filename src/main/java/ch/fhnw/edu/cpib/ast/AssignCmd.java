package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AssignCmd extends AbsSynTreeNode implements ICmd {
    private IExpr exprLeft;
    private IExpr exprRight;

    public AssignCmd(IExpr exprLeft, IExpr exprRight) {
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
        exprLeft.saveNamespaceInfoToNode(this.localStoresNamespace);
        exprRight.saveNamespaceInfoToNode(this.localStoresNamespace);

    }

    @Override public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
        exprLeft.doScopeChecking();
        exprRight.doScopeChecking();

        if (exprLeft.getLRValue() == LRValue.RVALUE)
            throw new LRValueError(LRValue.LVALUE, exprLeft.getLRValue());
    }

    @Override public void doTypeChecking() throws TypeCheckError, CastError {
        exprLeft.doTypeChecking();
        exprRight.doTypeChecking();

        if (exprLeft.getType() != exprRight.getType()) //&& !isCastable(exprLeft.getType(), exprRight.getType()))
            throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // lets check if we try to write something into an already written constant
        // exprLeft can only be an Init-Factor
        InitFactor factor = (InitFactor) exprLeft;
        // is the variable already initialized (= written once) and is a constant?
        // if yes, we are writing to an already initialized constant --> not allowed
        TypeIdent typeIdent = null;
        if (this.localStoresNamespace.containsKey(factor.ident.getIdent()))
            typeIdent = this.localStoresNamespace.get(factor.ident.getIdent());
        if (globalStoresNamespace.containsKey(factor.ident.getIdent())) {
            typeIdent = globalStoresNamespace.get(factor.ident.getIdent());
        }
        // If this is a const and it is already initialized (once written to), throw an error
        if (typeIdent.getConst() && typeIdent.getInit())
            throw new CannotAssignToConstError(factor.ident);

        exprLeft.doInitChecking(globalProtected);
        exprRight.doInitChecking(globalProtected);
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Get the address of the left expression
        InitFactor factor = (InitFactor) exprLeft;
        int address;
        if (!simulateOnly) {
            if (globalStoresLocation.containsKey(factor.ident.getIdent())) {
                address = globalStoresLocation.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(factor.ident.getIdent())) {
                address = localLocations.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No location found for variable " + factor.ident.getIdent() + " ?");
            }
        }
        codeArrayPointer++;

        // If this needs to be dereferenced (=Param), dereference it once more
        TypeIdent variableIdent = null;
        if (globalStoresNamespace.containsKey(factor.ident.getIdent())) {
            variableIdent = globalStoresNamespace.get(factor.ident.getIdent());
        } else {
            variableIdent = localStoresNamespace.get(factor.ident.getIdent());
        }
        if (variableIdent.getNeedToDeref()) {
            if (!simulateOnly)
                codeArray.put(codeArrayPointer, new IInstructions.Deref());
            codeArrayPointer++;
        }

        // Get the value of the exprRight (RVal)
        exprRight.addIInstrToCodeArray(localLocations, simulateOnly);

        // Now copy our value to the "remote" stack place (store)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.Store());
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
