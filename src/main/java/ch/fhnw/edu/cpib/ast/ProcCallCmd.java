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

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;

        // add local storage on everery case
        for (IExpr ie : expressions)
            ie.saveNamespaceInfoToNode(this.localStoresNamespace);
    }

    @Override public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
        // Check if this procedure identifier is declared in the global namespace
        boolean declared = globalRoutinesNamespace.containsKey(ident.getIdent());
        // If procedure is not declared in global namespace, throw exception
        if (!declared)
            throw new NameNotDeclaredError(ident.getIdent());

        // check scope for each expression
        for (IExpr expr : expressions) {
            expr.doScopeChecking();
        }

        // Param check
        // Same number of parameters as in declaration?
        ProcDecl procDecl = (ProcDecl) globalRoutinesNamespace.get(ident.getIdent());
        int sizeFound = expressions.size();
        int sizeExpected = procDecl.getParams().size();
        if (sizeExpected != sizeFound)
            throw new InvalidParamCountError(sizeExpected, sizeFound);

        // Check if r- and l-value of parameters are correct
        for (int i = 0; i < procDecl.getParams().size(); i++) {
            LRValue lrValueExpected = procDecl.getParams().get(i).getLRValue();
            LRValue lrValFound = expressions.get(i).getLRValue();
            if (lrValueExpected == LRValue.LVALUE && lrValFound == LRValue.RVALUE)
                throw new LRValueError(lrValueExpected, lrValFound);
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

        for (int i = 0; i < expressions.size(); i++) {
            LRValue callerLRVal = expressions.get(i).getLRValue();
            LRValue calleeLRVal = procDecl.getParams().get(i).getLRValue();
            // callee wants a RVAL, so we can pass either an RVAL or LVAL (will be used as value)
            if (calleeLRVal == LRValue.RVALUE) {
                expressions.get(i).addIInstrToCodeArray(localLocations, simulateOnly);

                // calee wants a LVAL, so it's only valid to pass an LVAL
            } else if (callerLRVal == LRValue.LVALUE && calleeLRVal == LRValue.LVALUE) {
                // Only LVal we have is a InitFactor
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
                        throw new RuntimeException("No location found for variable " + factor.ident.getIdent() + " !!");
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

                // callee wants an LVAL, but an RVAL is passed (invalid)
            } else {
                throw new RuntimeException("caller.RVAL not supported for callee.LVAL");
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
