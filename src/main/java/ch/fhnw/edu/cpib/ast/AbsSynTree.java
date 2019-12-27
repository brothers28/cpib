package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.cst.interfaces.IProgram;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public class AbsSynTree {
    private AbsSynTreeNode root;

    public AbsSynTree(IProgram concSynTreeRoot) {
        this.root = concSynTreeRoot.toAbsSyn();
    }

    @Override public String toString() {
        return root.toString("");
    }

    public void doScopeChecking()
            throws NameAlreadyDeclaredError, NameNotDeclaredError, NameAlreadyGloballyDeclaredError, LRValueError,
            AlreadyInitializedError, InvalidParamCountError {
        root.saveNamespaceInfoToNode(null);
        root.doScopeChecking();
    }

    public void doTypeChecking() throws TypeCheckError, CastError {
        root.doTypeChecking();
    }

    public void doInitChecking()
            throws NotInitializedError, AlreadyInitializedError, GlobalInitializationProhibitedError,
            CannotAssignToConstError {
        root.doInitChecking(false);
    }

    public ICodeArray getCodeArray() throws CodeTooSmallError {
        root.addIInstrToCodeArray(new HashMap<String, Integer>(), false);
        AbsSynTreeNode.codeArray.resize();
        return AbsSynTreeNode.codeArray;
    }

}
