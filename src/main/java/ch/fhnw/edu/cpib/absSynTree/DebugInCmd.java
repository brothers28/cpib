package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class DebugInCmd extends AbsSynTreeNode implements ICmd {
    private IExpr expr;

    public DebugInCmd(IExpr expr) {
        this.expr = expr;
    }

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
        expr.saveNamespaceInfoToNode(this.localStoresNamespace);
    }

    @Override public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
        expr.doScopeChecking();
        // We need an
        if (expr.getLRValue() != LRValue.LVALUE)
            throw new LRValueError(LRValue.LVALUE, expr.getLRValue());
    }

    @Override public void doTypeChecking() throws TypeCheckError {
        expr.doTypeChecking();
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // now lets check if we try to write something into an already written constant
        // expr can only be an Init-Factor
        InitFactor factor = (InitFactor) expr;
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

        expr.doInitChecking(globalProtected);

    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        InitFactor factor = (InitFactor) expr;
        // Get address
        int address;
        if (!simulateOnly) {
            if (globalStoresLocation.containsKey(factor.ident.getIdent())) {
                address = globalStoresLocation.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(factor.ident.getIdent())) {
                address = localLocations.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No location found for variable " + factor.ident.getIdent() + " !!");
            }
        }
        codeArrayPointer++;
        if (!simulateOnly) {
            if (factor.getType() == Types.BOOL) {
                codeArray.put(codeArrayPointer, new IInstructions.InputBool(factor.ident.getIdent()));
            } else if (factor.getType() == Types.INT64) {
                codeArray.put(codeArrayPointer, new IInstructions.InputInt(factor.ident.getIdent()));
            } else if (factor.getType() == Types.NAT64) {
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
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);

        return s;
    }
}
