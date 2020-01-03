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

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        expr.executeScopeCheck();
        // Has to be LVALUE
        if (expr.getLRValue() != LRValue.LVALUE)
            throw new LRValError(LRValue.LVALUE, expr.getLRValue());
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        expr.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        // exprLeft has to be InitFactor
        InitFactor factor = (InitFactor) expr;

        // Check if variable already initialized and a constant
        TypedIdent typedIdent = null;
        if (this.localVarNamespace.containsKey(factor.ident.getIdent()))
            typedIdent = this.localVarNamespace.get(factor.ident.getIdent());
        if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
            typedIdent = globalVarNamespace.get(factor.ident.getIdent());
        }
        // Constant and already initialized
        if (typedIdent.getConst() && typedIdent.getInit())
            throw new AssignToConstError(factor.ident);

        expr.executeInitCheck(globalProtected);

    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
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
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);

        return s;
    }
}
