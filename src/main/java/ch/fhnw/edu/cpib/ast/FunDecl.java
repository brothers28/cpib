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
    protected Ident ident;
    protected CpsCmd cpsCmd;
    protected ArrayList<Param> params;
    protected StoDecl stoDecl;
    protected ArrayList<StoDecl> stoDecls;
    protected boolean initChecked = false;

    public FunDecl(Ident ident, ArrayList<Param> params, StoDecl stoDecl, ArrayList<StoDecl> stoDecls, CpsCmd cpsCmd) {
        this.ident = ident;
        this.cpsCmd = cpsCmd;
        this.stoDecl = stoDecl;
        this.params = params;
        this.stoDecls = stoDecls;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {

        // Create local namespace
        this.localVarNamespace = new HashMap<>();
        for (Param param : params) {
            if (globalVarNamespace.containsKey(param.getIdentString()) || this.localVarNamespace
                    .containsKey(param.getIdentString())) {
                // Already declared
                throw new AlreadyDeclaredError(param.getIdentString());
            }
            // Deref
            param.getTypedIdent().setNeedToDeref();

            // Save to local namespace
            this.localVarNamespace.put(param.getIdentString(), param.getTypedIdent());
        }

        // Check result
        if (globalVarNamespace.containsKey(stoDecl.getIdentString()) || this.localVarNamespace
                .containsKey(stoDecl.getIdentString())) {
            // Already declared
            throw new AlreadyDeclaredError(stoDecl.getIdentString());
        }
        // Save result to local namespace
        this.localVarNamespace.put(stoDecl.getIdentString(), stoDecl.getTypedIdent());

        for (StoDecl stoDecl : stoDecls) {
            if (globalVarNamespace.containsKey(stoDecl.getIdentString()) || this.localVarNamespace
                    .containsKey(stoDecl.getIdentString())) {
                // Already declared
                throw new AlreadyDeclaredError(stoDecl.getIdentString());
            }

            // Save to local namespace
            this.localVarNamespace.put(stoDecl.getIdentString(), stoDecl.getTypedIdent());
        }

        cpsCmd.setNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        cpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        cpsCmd.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        cpsCmd.executeInitCheck(globalProtected);
    }

    @Override public String getIdentString() {
        return ident.getIdent();
    }

    public Types getReturnType() {
        return stoDecl.getTypedIdent().getType();
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public boolean getInitChecked() {
        return initChecked;
    }

    public void setInitChecked() {
        this.initChecked = true;
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        localLocations = new HashMap<>();

        // Initialize return value
        localLocations.put(stoDecl.getIdentString(), -params.size() - 1);

        // Add addresses of params to local address map
        for (int i = 0; i < params.size(); i++) {
            localLocations.put(params.get(i).getIdentString(), i - params.size());
        }

        // Add variables to local address map
        for (int i = 0; i < stoDecls.size(); i++) {
            stoDecls.get(i).addToCodeArray(localLocations, noExec);
            localLocations.put(stoDecls.get(i).getIdentString(), i + 3);
        }

        cpsCmd.addToCodeArray(localLocations, noExec);

        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.Return(params.size()));
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
        s += argumentIndendation + "<cpsCmd>:";
        s += cpsCmd.toString(lowerSpaces);
        s += argumentIndendation + "<params>:\n";
        for (Param param : params) {
            s += param.toString(lowerSpaces);
        }
        s += argumentIndendation + "<stoDecl>:";
        s += stoDecl.toString(lowerSpaces);
        s += argumentIndendation + "<stoDecls>:\n";
        for (StoDecl stoDecl : stoDecls) {
            s += stoDecl.toString(lowerSpaces);
        }
        return s;
    }
}
