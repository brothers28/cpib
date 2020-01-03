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
        TypeIdent typeIdent;
        if (localVarNamespace.containsKey(ident.getIdent())) {
            typeIdent = localVarNamespace.get(ident.getIdent());
        } else {
            typeIdent = globalVarNamespace.get(ident.getIdent());
        }
        return typeIdent.getType();
    }

    @Override public void executeTypeCheck() throws TypeCheckingError {
        //
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError,
            CannotAssignToConstError {
        // Get the typeIdent for this factor
        TypeIdent typeIdent = null;
        boolean isGlobal = false;
        if (this.localVarNamespace.containsKey(ident.getIdent()))
            typeIdent = this.localVarNamespace.get(ident.getIdent());
        if (globalVarNamespace.containsKey(ident.getIdent())) {
            typeIdent = globalVarNamespace.get(ident.getIdent());
            isGlobal = true;
        }
        if (init) {
            if (typeIdent.getInit()) {
                // Already inizialized
                throw new AlreadyInitializedError(typeIdent.getIdent());
            } else {
                typeIdent.setInit();
            }
        } else {
            if (!typeIdent.getInit())
                // Not initialized
                throw new NotInitializedError(typeIdent.getIdent());
        }
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Only LVal we have is a InitFactor
        // Get the address
        if (!simulateOnly) {
            int address;
            if (globalVarAdresses.containsKey(ident.getIdent())) {
                address = globalVarAdresses.get(ident.getIdent());
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
            codeArray.put(codeArrayPointer, new IInstructions.Deref(getType()));
        codeArrayPointer++;

        // If this needs to be dereferenced (=Param), dereference it once more
        TypeIdent variableIdent = null;
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
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<init>: " + init + "\n";

        return s;
    }

}
