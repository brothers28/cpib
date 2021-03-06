package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AssignCmd extends AstNode implements ICmd {
    private IExpr exprLeft;
    private IExpr exprRight;

    public AssignCmd(IExpr exprLeft, IExpr exprRight) {
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.saveNamespaceInfo(this.localVarNamespace);
        exprRight.saveNamespaceInfo(this.localVarNamespace);

    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        exprLeft.doScopeChecking();
        exprRight.doScopeChecking();

        // Has to be LVALUE
        if (exprLeft.getLRValue() == LRValue.RVALUE)
            throw new LRValueError(LRValue.LVALUE, exprLeft.getLRValue());
    }

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        exprLeft.doTypeChecking();
        exprRight.doTypeChecking();

        // Check allowed types
        if (exprLeft.getType() != exprRight.getType()) //&& !isCastable(exprLeft.getType(), exprRight.getType()))
            throw new TypeCheckingError(exprLeft.getType(), exprRight.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        // lets check if we try to write something into an already written constant
        // exprLeft can only be an Init-Factor
        InitFactor factor = (InitFactor) exprLeft;
        // is the variable already initialized (= written once) and is a constant?
        // if yes, we are writing to an already initialized constant --> not allowed
        TypeIdent typeIdent = null;
        if (this.localVarNamespace.containsKey(factor.ident.getIdent()))
            typeIdent = this.localVarNamespace.get(factor.ident.getIdent());
        if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
            typeIdent = globalVarNamespace.get(factor.ident.getIdent());
        }
        // If this is a const and it is already initialized (once written to), throw an error
        if (typeIdent.getConst() && typeIdent.getInit())
            throw new CannotAssignToConstError(factor.ident);

        exprLeft.doInitChecking(globalProtected);
        exprRight.doInitChecking(globalProtected);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Get the address of the left expression
        InitFactor factor = (InitFactor) exprLeft;
        int address;
        if (!simulateOnly) {
            if (globalVarAdresses.containsKey(factor.ident.getIdent())) {
                address = globalVarAdresses.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(factor.ident.getIdent())) {
                address = localLocations.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No address found for variable " + factor.ident.getIdent() + " ?");
            }
        }
        codeArrayPointer++;

        // If this needs to be dereferenced (=Param), dereference it once more
        TypeIdent variableIdent = null;
        if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
            variableIdent = globalVarNamespace.get(factor.ident.getIdent());
        } else {
            variableIdent = localVarNamespace.get(factor.ident.getIdent());
        }
        if (variableIdent.getNeedToDeref()) {
            if (!simulateOnly)
                codeArray.put(codeArrayPointer, new IInstructions.Deref());
            codeArrayPointer++;
        }

        // Get the value of the exprRight (RVal)
        exprRight.addInstructionToCodeArray(localLocations, simulateOnly);

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
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<exprLeft>:\n";
        s += exprLeft.toString(subIndent);
        s += argumentIndent + "<exprRight>:\n";
        s += exprRight.toString(subIndent);

        return s;
    }
}
