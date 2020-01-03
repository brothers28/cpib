package ch.fhnw.edu.cpib.ast;

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

public class FunCallFactor extends IdentFactor {
    private ArrayList<IExpr> expressions;
    private Types castType;

    public FunCallFactor(Ident ident, ArrayList<IExpr> expressions) {
        this.ident = ident;
        this.expressions = expressions;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        for (IExpr expr : expressions) {
            expr.setNamespaceInfo(this.localVarNamespace);
        }
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
        FunDecl funDecl = (FunDecl) globalRoutNamespace.get(ident.getIdent());
        int realSize = expressions.size();
        int expectedSize = funDecl.getParams().size();
        if (expectedSize != realSize)
            // Not same number of parameters as declared
            throw new InvalidParamCountError(expectedSize, realSize);

        // Check LRValue
        for (int i = 0; i < funDecl.getParams().size(); i++) {
            LRValue expectedLRValue = funDecl.getParams().get(i).getLRValue();
            LRValue realLRValue = expressions.get(i).getLRValue();
            if (expectedLRValue == LRValue.LVALUE && realLRValue == LRValue.RVALUE)
                // We expect LVALUE, but get RVALUE (invalid)
                throw new LRValError(expectedLRValue, realLRValue);
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        for (IExpr expr : expressions) {
            expr.executeTypeCheck();
        }

        // Check allowed types
        FunDecl funDecl = (FunDecl) globalRoutNamespace.get(ident.getIdent());
        for (int i = 0; i < funDecl.getParams().size(); i++) {
            Types expectedType = funDecl.getParams().get(i).getTypedIdent().getType();
            Types realType = expressions.get(i).getType();
            if (expectedType != realType)
                throw new TypeCheckError(expectedType, realType);
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        // Check function declaration
        FunDecl funDecl = (FunDecl) globalRoutNamespace.get(ident.getIdent());

        // Check only once
        if (!funDecl.getInitChecked()) {
            funDecl.setInitChecked();
            funDecl.executeInitCheck(globalProtected);
        }

        // Run on expressions
        for (IExpr expr : expressions) {
            expr.executeInitCheck(globalProtected);
        }
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
        for (IExpr expr : expressions) {
            expr.executeTypeCast(type);
        }
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        FunDecl funDecl = (FunDecl) globalRoutNamespace.get(ident.getIdent());
        return funDecl.getReturnType();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {

        FunDecl funDecl = (FunDecl) globalRoutNamespace.get(ident.getIdent());
        // Initialize return value
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
        codeArrayPointer++;

        // LR checking
        for (int i = 0; i < expressions.size(); i++) {
            LRValue realLRValue = expressions.get(i).getLRValue();
            LRValue expectedLRValue = funDecl.getParams().get(i).getLRValue();
            if (expectedLRValue == LRValue.RVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE
                expressions.get(i).addToCodeArray(localLocations, noExec);
            } else if (realLRValue == LRValue.LVALUE && expectedLRValue == LRValue.LVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE

                // Get InitFactor
                InitFactor factor = (InitFactor) expressions.get(i);
                // Get address
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

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<expressions>:\n";
        for (IExpr expr : expressions) {
            s += expr.toString(subIndent);
        }

        return s;
    }

}
