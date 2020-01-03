package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IAstNode;
import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.vm.CodeArray;
import ch.fhnw.edu.cpib.vm.ICodeArray;

import java.util.HashMap;

public abstract class AstNode implements IAstNode {

    // Namespaces
    static HashMap<String, TypeIdent> globalVarNamespace = new HashMap<>();
    static HashMap<String, IDecl> globalRoutNamespace = new HashMap<>();
    // Adresses
    static HashMap<String, Integer> globalVarAdresses = new HashMap<>();
    static HashMap<String, Integer> globalRoutAdresses = new HashMap<>();
    // Code array
    static int codeArrayPointer = 0;
    static ICodeArray codeArray = new CodeArray(1024);
    HashMap<String, TypeIdent> localVarNamespace = new HashMap<>();

    public void setInit(TypeIdent ident) {
        if (localVarNamespace.containsKey(ident.getValue()))
            localVarNamespace.get(ident.getValue()).setInit();
    }

}
