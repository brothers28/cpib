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
    protected Ident ident;
    protected ArrayList<IExpr> expressions = new ArrayList<>();

    public ProcCallCmd(Ident ident, ArrayList<IExpr> expressions) {
        this.ident = ident;
        this.expressions = expressions;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;

        // add local storage on everery case
        for (IExpr ie : expressions)
            ie.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
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
                throw new LRValError(expectedLRValue, realRLValue);
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        for (IExpr expr : expressions) {
            expr.executeTypeCheck();
        }

        // Check allowed types
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());
        for (int i = 0; i < procDecl.getParams().size(); i++) {
            Types expectedType = procDecl.getParams().get(i).getTypedIdent().getType();
            Types realType = expressions.get(i).getType();
            if (expectedType != realType && !isCastable(expectedType, realType))
                throw new TypeCheckError(expectedType, realType);
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {

        // Init check for procedure declaration
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());

        // Check only once
        if (!procDecl.getInitChecked()) {
            procDecl.setInitChecked();
            procDecl.executeInitCheck(globalProtected);
        }

        // Run on expressions
        for (IExpr expr : expressions) {
            expr.executeInitCheck(globalProtected);
        }
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        ProcDecl procDecl = (ProcDecl) globalRoutNamespace.get(ident.getIdent());

        // LR checking
        for (int i = 0; i < expressions.size(); i++) {
            LRValue realLRValue = expressions.get(i).getLRValue();
            LRValue expectedLRValue = procDecl.getParams().get(i).getLRValue();
            if (expectedLRValue == LRValue.RVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE
                expressions.get(i).addToCodeArray(localLocations, noExec);
            } else if (realLRValue == LRValue.LVALUE && expectedLRValue == LRValue.LVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE

                // Get InitFactor
                InitFactor factor = (InitFactor) expressions.get(i);
                // Get the address
                if (!noExec) {
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
                TypedIdent variableIdent = null;
                if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
                    variableIdent = globalVarNamespace.get(factor.ident.getIdent());
                } else {
                    variableIdent = localVarNamespace.get(factor.ident.getIdent());
                }
                if (variableIdent.getNeedToDeref()) {
                    if (!noExec)
                        codeArray.put(codeArrayPointer, new IInstructions.Deref());
                    codeArrayPointer++;
                }
            } else {
                // We expect LVALUE, but get RVALUE (invalid)
                throw new RuntimeException("LValue expected but RValue given");
            }
        }

        if (!noExec) {
            int funAddress = globalRoutAdresses.get(ident.getIdent());
            codeArray.put(codeArrayPointer, new IInstructions.Call(funAddress));
        }
        codeArrayPointer++;
    }

    @Override public String toString(String spaces) {
        // Set horizontal spaces
        String identifierIndendation = spaces;
        String argumentIndendation = spaces + " ";
        String lowerSpaces = spaces + "  ";

        // Get class
        String s = "";
        s += identifierIndendation + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndendation + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndendation + "<ident>: " + ident.toString() + "\n";
        s += argumentIndendation + "<expressions>:\n";
        for (IExpr expr : expressions) {
            s += expr.toString(lowerSpaces);
        }

        return s;
    }
}
