package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class DebugInCmd extends AstNode implements ICmd {
    private IExpr expr;

    public DebugInCmd(IExpr expr) {
        this.expr = expr;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        expr.doScopeChecking();
        // Has to be LVALUE
        if (expr.getLRValue() != LRValue.LVALUE)
            throw new LRValueError(LRValue.LVALUE, expr.getLRValue());
    }

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        expr.doTypeChecking();
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        // now lets check if we try to write something into an already written constant
        // expr can only be an Init-Factor
        InitFactor factor = (InitFactor) expr;
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

        expr.doInitChecking(globalProtected);

    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        InitFactor factor = (InitFactor) expr;
        // Get address
        int address;
        if (!simulateOnly) {
            if (globalVarAdresses.containsKey(factor.ident.getIdent())) {
                address = globalVarAdresses.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(factor.ident.getIdent())) {
                address = localLocations.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No address found for variable " + factor.ident.getIdent() + " !!");
            }
        }
        codeArrayPointer++;
        if (!simulateOnly) {
            if (factor.getType() == Types.BOOL) {
                codeArray.put(codeArrayPointer, new IInstructions.InputBool(factor.ident.getIdent()));
            } else if (factor.getType() == Types.INT32) {
                codeArray.put(codeArrayPointer, new IInstructions.InputInt(factor.ident.getIdent()));
            } else if (factor.getType() == Types.NAT32) {
                codeArray.put(codeArrayPointer, new IInstructions.InputNat(factor.ident.getIdent()));
            } else {
                throw new RuntimeException("Unknown Type!");
            }
        }
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
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);

        return s;
    }
}
