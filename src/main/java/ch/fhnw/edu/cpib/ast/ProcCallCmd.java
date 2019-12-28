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
        this.localStoresNamespace = localStoresNamespace;

        // add local storage on everery case
        for (IExpr ie : expressions)
            ie.saveNamespaceInfo(this.localStoresNamespace);
    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        if (!globalRoutinesNamespace.containsKey(ident.getIdent())) {
            // Function not declared in global namespace
            throw new NotDeclaredError(ident.getIdent());
        }

        // Check scope
        for (IExpr expr : expressions) {
            expr.doScopeChecking();
        }

        // Check param
        ProcDecl procDecl = (ProcDecl) globalRoutinesNamespace.get(ident.getIdent());
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

    @Override public void doTypeChecking() throws TypeCheckError, CastError {
        for (IExpr expr : expressions) {
            expr.doTypeChecking();
        }

        ProcDecl procDecl = (ProcDecl) globalRoutinesNamespace.get(ident.getIdent());
        for (int i = 0; i < procDecl.getParams().size(); i++) {
            Types typeExpected = procDecl.getParams().get(i).getTypeIdent().getType();
            Types typeFound = expressions.get(i).getType();
            if (typeExpected != typeFound && !isCastable(typeExpected, typeFound))
                throw new TypeCheckError(typeExpected, typeFound);
        }
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // Run the init checking for the function declaration
        ProcDecl procDecl = (ProcDecl) globalRoutinesNamespace.get(ident.getIdent());
        // We need to run the init checking only once for the declaration
        if (!procDecl.getInitCheckDone()) {
            procDecl.setInitCheckDone();
            procDecl.doInitChecking(globalProtected);
        }

        for (IExpr expr : expressions) {
            expr.doInitChecking(globalProtected);
        }
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        ProcDecl procDecl = (ProcDecl) globalRoutinesNamespace.get(ident.getIdent());

        // LR checking
        for (int i = 0; i < expressions.size(); i++) {
            LRValue realLRValue = expressions.get(i).getLRValue();
            LRValue expectedLRValue = procDecl.getParams().get(i).getLRValue();
            if (expectedLRValue == LRValue.RVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE
                expressions.get(i).addIInstrToCodeArray(localLocations, simulateOnly);
            } else if (realLRValue == LRValue.LVALUE && expectedLRValue == LRValue.LVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE

                // Get InitFactor
                InitFactor factor = (InitFactor) expressions.get(i);
                // Get the address
                if (!simulateOnly) {
                    int address;
                    if (globalStoresLocation.containsKey(factor.ident.getIdent())) {
                        address = globalStoresLocation.get(factor.ident.getIdent());
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
            } else {
                // We expect LVALUE, but get RVALUE (invalid)
                throw new RuntimeException("LValue expected but RValue given");
            }
        }

        if (!simulateOnly) {
            int funAddress = globalRoutinesLocation.get(ident.getIdent());
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
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<ident>: " + ident.toString() + "\n";
        s += argumentIndent + "<expressions>:\n";
        for (IExpr expr : expressions) {
            s += expr.toString(subIndent);
        }

        return s;
    }
}
