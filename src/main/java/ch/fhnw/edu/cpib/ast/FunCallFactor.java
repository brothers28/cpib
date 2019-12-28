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

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
        for (IExpr expr : expressions) {
            expr.saveNamespaceInfoToNode(this.localStoresNamespace);
        }
    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        if (!globalRoutinesNamespace.containsKey(ident.getIdent())) {
            // Function not declared in global namespace
            throw new NotDeclaredError(ident.getIdent());}

        // Check scope
        for (IExpr expr : expressions) {
            expr.doScopeChecking();
        }

        // Check param
        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
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
                throw new LRValueError(expectedLRValue, realLRValue);
        }
    }

    @Override public void doTypeCasting(Types type) {
        if (type != null) {
            this.castType = type;
        }
        for (IExpr expr : expressions) {
            expr.doTypeCasting(type);
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
        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
        return funDecl.getReturnType();
    }

    @Override public void doTypeChecking() throws TypeCheckError, CastError {
        for (IExpr expr : expressions) {
            expr.doTypeChecking();
        }

        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
        for (int i = 0; i < funDecl.getParams().size(); i++) {
            Types typeExpected = funDecl.getParams().get(i).getTypeIdent().getType();
            Types typeFound = expressions.get(i).getType();
            if (typeExpected != typeFound)
                throw new TypeCheckError(typeExpected, typeFound);
        }
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // Run the init checking for the function declaration
        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
        // We need to run the init checking only once for the declaration
        if (!funDecl.getInitCheckDone()) {
            funDecl.setInitCheckDone();
            funDecl.doInitChecking(globalProtected);
        }

        for (IExpr expr : expressions) {
            expr.doInitChecking(globalProtected);
        }
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
        // initialize return value
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.AllocBlock(1));
        codeArrayPointer++;

        // LR checking
        for (int i = 0; i < expressions.size(); i++) {
            LRValue realLRValue = expressions.get(i).getLRValue();
            LRValue expectedLRValue = funDecl.getParams().get(i).getLRValue();
            if (expectedLRValue == LRValue.RVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE
                expressions.get(i).addIInstrToCodeArray(localLocations, simulateOnly);
            } else if (realLRValue == LRValue.LVALUE && expectedLRValue == LRValue.LVALUE) {
                // We expect RVALUE, pass RVALUE or LVALUE

                // Get InitFactor
                InitFactor factor = (InitFactor) expressions.get(i);
                // Get address
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
        s += argumentIndent + "<expressions>:\n";
        for (IExpr expr : expressions) {
            s += expr.toString(subIndent);
        }

        return s;
    }

}
