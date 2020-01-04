package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TypedIdent extends AstNode implements Cloneable {
    protected Ident ident;
    protected Types type;
    protected boolean isInit;
    protected boolean isConst;
    protected boolean needToDeref;

    public TypedIdent(Ident ident, Types type) {
        this.ident = ident;
        this.type = type;
    }

    @Override public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getValue() {
        return ident.getIdent();
    }

    public Ident getIdent() {
        return ident;
    }

    public Types getType() {
        return type;
    }

    public void setInit() {
        this.isInit = true;
    }

    public boolean getInit() {
        return isInit;
    }

    public boolean getConst() {
        return isConst;
    }

    public void setConst() {
        this.isConst = true;
    }

    public void setNeedToDeref() {
        this.needToDeref = true;
    }

    public boolean getNeedToDeref() {
        return needToDeref;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void executeScopeCheck() throws NotDeclaredError {
        //
    }

    @Override public void executeTypeCheck() throws TypeCheckError {
        //
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        //
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        //
    }

    @Override public String toString(String spaces) {
        String identifierIndendation = spaces;
        String argumentIndendation = spaces + " ";
        String s = "";
        s += identifierIndendation + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndendation + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndendation + "(<ident>, <type>): (" + ident.toString() + ", " + type.toString() + ")\n";

        return s;
    }
}
