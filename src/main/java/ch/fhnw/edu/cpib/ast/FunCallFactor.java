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
            throws NameAlreadyDeclaredError, NameAlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
        for (IExpr expr : expressions) {
            expr.saveNamespaceInfoToNode(this.localStoresNamespace);
        }
    }

    @Override public void doScopeChecking() throws NameNotDeclaredError, LRValueError, InvalidParamCountError {
        // Check if this function identifier is declared in the global namespace
        boolean declared = globalRoutinesNamespace.containsKey(ident.getIdent());
        // If function is not declared in global namespace, throw exception
        if (!declared)
            throw new NameNotDeclaredError(ident.getIdent());

        // check scope for each expression
        for (IExpr expr : expressions) {
            expr.doScopeChecking();
        }

        // Param check
        // Same number of parameters as in declaration?
        FunDecl funDecl = (FunDecl) globalRoutinesNamespace.get(ident.getIdent());
        int sizeFound = expressions.size();
        int sizeExpected = funDecl.getParams().size();
        if (sizeExpected != sizeFound)
            throw new InvalidParamCountError(sizeExpected, sizeFound);

        // Check if r- and l-value of parameters are correct
        for (int i = 0; i < funDecl.getParams().size(); i++) {
            LRValue lrValExpected = funDecl.getParams().get(i).getLRValue();
            LRValue lrValFound = expressions.get(i).getLRValue();
            if (lrValExpected == LRValue.LVALUE && lrValFound == LRValue.RVALUE)
                throw new LRValueError(lrValExpected, lrValFound);
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

        for (int i = 0; i < expressions.size(); i++) {
            LRValue callerLRVal = expressions.get(i).getLRValue();
            LRValue calleeLRVal = funDecl.getParams().get(i).getLRValue();
            // callee wants a RVALUE, so we can pass either an RVALUE or LVALUE (will be used as value)
            if (calleeLRVal == LRValue.RVALUE) {
                expressions.get(i).addIInstrToCodeArray(localLocations, simulateOnly);

                // calee wants a LVALUE, so it's only valid to pass an LVALUE
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
                // callee wants an LVALUE, but an RVALUE is passed (invalid)
            } else {
                throw new RuntimeException("caller.RVALUE not supported for callee.LVALUE");
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
