package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TypeIdent extends AstNode implements Cloneable {
    private Ident ident;
    private Types type;
    private boolean isInit;
    private boolean isConst;
    private boolean needToDeref;

    public TypeIdent(Ident ident, Types type) {
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

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
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
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        //
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        //
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "(<ident>, <type>): (" + ident.toString() + ", " + type.toString() + ")\n";

        return s;
    }
}
