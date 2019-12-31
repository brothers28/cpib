package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static ch.fhnw.edu.cpib.parser.util.CastChecker.isCastable;

public class ProcCallCmd extends AstNode implements ICmd {
    private Ident ident;
    private ArrayList<IExpr> expressions = new ArrayList<>();

    public ProcCallCmd(Ident ident, ArrayList<IExpr> expressions) {
        this.ident = ident;
        this.expressions = expressions;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;

        // add local storage on everery case
        for (IExpr ie : expressions)
            ie.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        // Check namespace
        if (!globalRoutNamespace.containsKey(ident.getIdent())) {
            // Function not declared in global namespace
            throw new NotDeclaredError(ident.getIdent());
        }

        // Check scope
        for (IExpr expr : expressions) {
            expr.executeScopeCheck();
        }

        // Check param
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());
        int realSize = expressions.size();
        int expectedSize = procDecl.getParams().size();
        if (expectedSize != realSize)
            // Not same number of parameters as declared
            throw new InvalidParamCountError(expectedSize, realSize);

        // Check LRValue
        for (int i = 0; i < procDecl.getParams().size(); i++) {
            LRValue expectedLRValue = procDecl.getParams().get(i).getLRValue();
            LRValue realRLValue = expressions.get(i).getLRValue();
            if (expectedLRValue == LRValue.LVALUE && realRLValue == LRValue.RVALUE)
                // We expect LVALUE, but get RVALUE (invalid)
                throw new LRValueError(expectedLRValue, realRLValue);
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckingError, CastError {
        for (IExpr expr : expressions) {
            expr.executeTypeCheck();
        }

        // Check allowed types
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());
        for (int i = 0; i < procDecl.getParams().size(); i++) {
            Types expectedType = procDecl.getParams().get(i).getTypeIdent().getType();
            Types realType = expressions.get(i).getType();
            if (expectedType != realType && !isCastable(expectedType, realType)) // TODO: Uncomment isCastable like in AssignCmd?
                throw new TypeCheckingError(expectedType, realType);
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        // Run the init checking for the function declaration
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());
        // We need to run the init checking only once for the declaration
        if (!procDecl.getInitCheckDone()) {
            procDecl.setInitCheckDone();
            procDecl.executeInitCheck(globalProtected);
        }

        for (IExpr expr : expressions) {
            expr.executeInitCheck(globalProtected);
        }
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());

        // LR checking
        for (int i = 0; i < expressions.size(); i++) {
            LRValue realLRValue = expressions.get(i).getLRValue();
            LRValue expectedLRValue = procDecl.getParams().get(i).getLRValue();
            if (expectedLRValue == LRValue.RVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE
                expressions.get(i).addInstructionToCodeArray(localLocations, simulateOnly);
            } else if (realLRValue == LRValue.LVALUE && expectedLRValue == LRValue.LVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE

                // Get InitFactor
                InitFactor factor = (InitFactor) expressions.get(i);
                // Get the address
                if (!simulateOnly) {
                    int address;
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

                // Deref
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
            } else {
                // We expect LVALUE, but get RVALUE (invalid)
                throw new RuntimeException("LValue expected but RValue given");
            }
        }

        if (!simulateOnly) {
            int funAddress = globalRoutAdresses.get(ident.getIdent());
            codeArray.put(codeArrayPointer, new IInstructions.Call(funAddress));
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
        s += argumentIndent + "<ident>: " + ident.toString() + "\n";
        s += argumentIndent + "<expressions>:\n";
        for (IExpr expr : expressions) {
            s += expr.toString(subIndent);
        }

        return s;
    }
}
