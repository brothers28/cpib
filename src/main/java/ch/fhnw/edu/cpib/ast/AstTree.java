package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.cst.interfaces.IProgram;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.HashMap;

public class AstTree {
    private AstNode root;

    public AstTree(IProgram concSynTreeRoot) {
        this.root = concSynTreeRoot.toAbsSyntax();
    }

    @Override public String toString() {
        return root.toString("");
    }

    public void doScopeChecking()
            throws AlreadyDeclaredError, NotDeclaredError, AlreadyGloballyDeclaredError, LRValueError,
            AlreadyInitializedError, InvalidParamCountError {
        root.saveNamespaceInfo(null);
        root.doScopeChecking();
    }

    public void doTypeChecking() throws TypeCheckingError, CastError {
        root.doTypeChecking();
    }

    public void doInitChecking()
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        root.doInitChecking(false);
    }

    public ICodeArray getCodeArray() throws CodeTooSmallError {
        root.addIInstrToCodeArray(new HashMap<String, Integer>(), false);
        AstNode.codeArray.resize();
        return AstNode.codeArray;
    }

}
