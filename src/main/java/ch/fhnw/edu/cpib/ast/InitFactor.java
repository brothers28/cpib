package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class InitFactor extends IdentFactor {
    private boolean init;
    private Types castType;

    public InitFactor(Ident ident, boolean init) {
        this.ident = ident;
        this.init = init;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void executeScopeCheck() throws NotDeclaredError {
        // Check namespace
        boolean declared = false;
        if (localVarNamespace.containsKey(ident.getIdent()))
            // Variable is declared in local namespace
            declared = true;
        if (globalVarNamespace.containsKey(ident.getIdent()))
            // Variable is declared in global namespace
            declared = true;
        if (!declared) {
            // Variable is not declared
            throw new NotDeclaredError(ident.getIdent());
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckError {
        //
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {

        // Get typedIdent for this factor
        TypedIdent typedIdent = null;
        boolean isGlobal = false;
        if (this.localVarNamespace.containsKey(ident.getIdent()))
            typedIdent = this.localVarNamespace.get(ident.getIdent());
        if (globalVarNamespace.containsKey(ident.getIdent())) {
            typedIdent = globalVarNamespace.get(ident.getIdent());
            isGlobal = true;
        }
        if (init) {
            if (globalProtected && isGlobal)
                // Global and protected variable cannot be initialized
                throw new GlobalProtectedInitializationError(typedIdent.getIdent());
            if (typedIdent.getInit()) {
                // Already initialized
                throw new AlreadyInitializedError(typedIdent.getIdent());
            } else {
                typedIdent.setInit();
            }
        } else {
            if (!typedIdent.getInit())
                // Not initialized
                throw new NotInitializedError(typedIdent.getIdent());
        }
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
    }

    @Override public LRValue getLRValue() {
        return LRValue.LVALUE;
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        TypedIdent typedIdent;
        if (localVarNamespace.containsKey(ident.getIdent())) {
            typedIdent = localVarNamespace.get(ident.getIdent());
        } else {
            typedIdent = globalVarNamespace.get(ident.getIdent());
        }
        return typedIdent.getType();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Get address of LValue
        if (!simulateOnly) {
            int address;
            if (globalVarAdresses.containsKey(ident.getIdent())) {
                address = globalVarAdresses.get(ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(ident.getIdent())) {
                address = localLocations.get(ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No address found for " + ident.getIdent() + " !!");
            }
        }
        codeArrayPointer++;

        // Deref
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.Deref(getType()));
        codeArrayPointer++;

        // Deref
        TypedIdent variableIdent = null;
        if (globalVarNamespace.containsKey(ident.getIdent())) {
            variableIdent = globalVarNamespace.get(ident.getIdent());
        } else {
            variableIdent = localVarNamespace.get(ident.getIdent());
        }
        if (variableIdent.getNeedToDeref()) {
            if (!simulateOnly)
                codeArray.put(codeArrayPointer, new IInstructions.Deref(getType()));
            codeArrayPointer++;
        }
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<init>: " + init + "\n";

        return s;
    }

}
