package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FunDecl extends AstNode implements IDecl {
    private Ident ident;
    private CpsCmd cpsCmd;
    private ArrayList<Param> params;
    private StoDecl stoDecl;
    private ArrayList<StoDecl> stoDecls;
    private boolean initCheckDone = false;

    public FunDecl(Ident ident, ArrayList<Param> params, StoDecl stoDecl, ArrayList<StoDecl> stoDecls, CpsCmd cpsCmd) {
        this.ident = ident;
        this.cpsCmd = cpsCmd;
        this.stoDecl = stoDecl;
        this.params = params;
        this.stoDecls = stoDecls;
    }

    public Types getReturnType() {
        return stoDecl.getTypedIdent().getType();
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public boolean getInitCheckDone() {
        return initCheckDone;
    }

    public void setInitCheckDone() {
        this.initCheckDone = true;
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        cpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        cpsCmd.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        cpsCmd.executeInitCheck(globalProtected);
    }

    @Override public String getIdentString() {
        return ident.getIdent();
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {

        // Create local namespace
        this.localVarNamespace = new HashMap<>();
        for (Param param : params) {
            if (globalVarNamespace.containsKey(param.getIdentString()))
                // Already declared (globally)
                throw new AlreadyGloballyDeclaredError(param.getIdentString());
            if (this.localVarNamespace.containsKey(param.getIdentString()))
                // Already declared
                throw new AlreadyDeclaredError(param.getIdentString());

            // Deref
            param.getTypedIdent().setNeedToDeref();

            // Save to local namespace
            this.localVarNamespace.put(param.getIdentString(), param.getTypedIdent());
        }

        // Check result
        if (globalVarNamespace.containsKey(stoDecl.getIdentString()))
            // Already declared (globally)
            throw new AlreadyGloballyDeclaredError(stoDecl.getIdentString());
        if (this.localVarNamespace.containsKey(stoDecl.getIdentString()))
            // Already declared
            throw new AlreadyDeclaredError(stoDecl.getIdentString());
        // Save result to local namespace
        this.localVarNamespace.put(stoDecl.getIdentString(), stoDecl.getTypedIdent());

        for (StoDecl stoDecl : stoDecls) {
            if (globalVarNamespace.containsKey(stoDecl.getIdentString()))
                // Already declared (globally)
                throw new AlreadyGloballyDeclaredError(stoDecl.getIdentString());
            if (this.localVarNamespace.containsKey(stoDecl.getIdentString()))
                // Already declared (globally)
                throw new AlreadyDeclaredError(stoDecl.getIdentString());

            // Save to local namespace
            this.localVarNamespace.put(stoDecl.getIdentString(), stoDecl.getTypedIdent());
        }

        cpsCmd.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        localLocations = new HashMap<>();

        // will be initialized from outside @ FunCallFactor
        // return value is one below the first param
        localLocations.put(stoDecl.getIdentString(), -params.size() - 1);

        // add addresses of params to localLocations-map
        for (int i = 0; i < params.size(); i++) {
            // will be initialized from outside @ FunCallFactor
            localLocations.put(params.get(i).getIdentString(), i - params.size());
        }

        // add addresses of local variables to localLocations-map
        // first local variable is at relAddress 3
        for (int i = 0; i < stoDecls.size(); i++) {
            stoDecls.get(i).addToCodeArray(localLocations, simulateOnly);
            localLocations.put(stoDecls.get(i).getIdentString(), i + 3);
        }

        cpsCmd.addToCodeArray(localLocations, simulateOnly);

        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.Return(params.size()));
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
        s += argumentIndent + "<ident>: " + ident.toString() + "\n";
        s += argumentIndent + "<cpsCmd>:";
        s += cpsCmd.toString(subIndent);
        s += argumentIndent + "<params>:\n";
        for (Param param : params) {
            s += param.toString(subIndent);
        }
        s += argumentIndent + "<stoDecl>:";
        s += stoDecl.toString(subIndent);
        s += argumentIndent + "<stoDecls>:\n";
        for (StoDecl stoDecl : stoDecls) {
            s += stoDecl.toString(subIndent);
        }
        return s;
    }
}
