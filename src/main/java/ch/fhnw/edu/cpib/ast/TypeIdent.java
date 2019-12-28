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

    @Override public void saveNamespaceInfoToNode(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localStoresNamespace = localStoresNamespace;
    }

    @Override public void doScopeChecking() throws NotDeclaredError {
        // Do nothing
    }

    @Override public void doTypeChecking() throws TypeCheckError {
        // Do nothing
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        // Do nothing
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // Do nothing
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localStoresNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localStoresNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "(<ident>, <type>): (" + ident.toString() + ", " + type.toString() + ")\n";

        return s;
    }
}
