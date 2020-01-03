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

    public void executeScopeCheck()
            throws AlreadyDeclaredError, NotDeclaredError, AlreadyGloballyDeclaredError, LRValError,
            AlreadyInitializedError, InvalidParamCountError {
        root.setNamespaceInfo(null);
        root.executeScopeCheck();
    }

    public void executeTypeCheck() throws TypeCheckError, CastError {
        root.executeTypeCheck();
    }

    public void executeInitCheck()
            throws NotInitializedError, AlreadyInitializedError,
            AssignToConstError {
        root.executeInitCheck(false);
    }

    public ICodeArray getCodeArray() throws CodeTooSmallError {
        root.addToCodeArray(new HashMap<String, Integer>(), false);
        AstNode.codeArray.resize();
        return AstNode.codeArray;
    }

}
