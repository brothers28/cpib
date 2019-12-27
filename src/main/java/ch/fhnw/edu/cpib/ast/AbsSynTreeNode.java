package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IAbsSynTreeNode;
import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.vm.CodeArray;
import ch.fhnw.edu.cpib.vm.ICodeArray;

import java.util.HashMap;

public abstract class AbsSynTreeNode implements IAbsSynTreeNode {

    static HashMap<String, TypeIdent> globalStoresNamespace = new HashMap<>();

    static HashMap<String, IDecl> globalRoutinesNamespace = new HashMap<>();

    HashMap<String, TypeIdent> localStoresNamespace = new HashMap<>();

    static HashMap<String, Integer> globalStoresLocation = new HashMap<>();

    static HashMap<String, Integer> globalRoutinesLocation = new HashMap<>();

    static int codeArrayPointer = 0;

    static ICodeArray codeArray = new CodeArray(1024);

    public void setInit(TypeIdent ident) {
        if (localStoresNamespace.containsKey(ident.getValue()))
            localStoresNamespace.get(ident.getValue()).setInit();
    }

}
