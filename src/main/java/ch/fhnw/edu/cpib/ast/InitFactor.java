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

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
    }

    @Override public void doScopeChecking() throws NotDeclaredError {
        boolean declared = false;
        if (localStoresNamespace.containsKey(ident.getIdent()))
            // Variable is declared in local namespace
            declared = true;
        if (globalStoresNamespace.containsKey(ident.getIdent()))
            // Variable is declared in global namespace
            declared = true;
        if (!declared) {
            // Variable is not declared
            throw new NotDeclaredError(ident.getIdent());
        }
    }

    @Override public void doTypeCasting(Types type) {
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
        TypeIdent typeIdent;
        if (localStoresNamespace.containsKey(ident.getIdent())) {
            typeIdent = localStoresNamespace.get(ident.getIdent());
        } else {
            typeIdent = globalStoresNamespace.get(ident.getIdent());
        }
        return typeIdent.getType();
    }

    @Override public void doTypeChecking() throws TypeCheckError {
        //
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // Get the typeIdent for this factor
        TypeIdent typeIdent = null;
        boolean isGlobal = false;
        if (this.localStoresNamespace.containsKey(ident.getIdent()))
            typeIdent = this.localStoresNamespace.get(ident.getIdent());
        if (globalStoresNamespace.containsKey(ident.getIdent())) {
            typeIdent = globalStoresNamespace.get(ident.getIdent());
            isGlobal = true;
        }
        if (init) {
            // If this is a global variable, and we try to initialize it in a protected scope, throw an error
            if (globalProtected && isGlobal)
                throw new GlobalInitializationProhibitedError(typeIdent.getIdent());
            // Throw an error if this typeIdent is already initialized
            if (typeIdent.getInit()) {
                throw new AlreadyInitializedError(typeIdent.getIdent());
            } else {
                typeIdent.setInit();
            }
        } else {
            // Throw an error if this typeIdent is not yet initialized and we try to use it
            if (!typeIdent.getInit())
                throw new NotInitializedError(typeIdent.getIdent());
        }
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Only LVal we have is a InitFactor
        // Get the address
        if (!simulateOnly) {
            int address;
            if (globalStoresLocation.containsKey(ident.getIdent())) {
                address = globalStoresLocation.get(ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(ident.getIdent())) {
                address = localLocations.get(ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No address found for variable " + ident.getIdent() + " !!");
            }
        }
        codeArrayPointer++;

        // Now copy the real value to this stack place (dereference)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.Deref());
        codeArrayPointer++;

        // If this needs to be dereferenced (=Param), dereference it once more
        TypeIdent variableIdent = null;
        if (globalStoresNamespace.containsKey(ident.getIdent())) {
            variableIdent = globalStoresNamespace.get(ident.getIdent());
        } else {
            variableIdent = localStoresNamespace.get(ident.getIdent());
        }
        if (variableIdent.getNeedToDeref()) {
            if (!simulateOnly)
                codeArray.put(codeArrayPointer, new IInstructions.Deref());
            codeArrayPointer++;
        }
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<init>: " + init + "\n";

        return s;
    }

}
