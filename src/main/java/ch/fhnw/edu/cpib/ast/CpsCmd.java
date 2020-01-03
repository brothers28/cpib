package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CpsCmd extends AstNode implements ICmd {
    private ArrayList<ICmd> commands;

    public CpsCmd(ArrayList<ICmd> commands) {
        this.commands = commands;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        for (ICmd cmd : commands) {
            cmd.saveNamespaceInfo(this.localVarNamespace);
        }
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        for (ICmd cmd : commands) {
            cmd.executeScopeCheck();
        }
    }

    @Override public void executeTypeCheck() throws TypeCheckingError, CastError {
        for (ICmd cmd : commands) {
            cmd.executeTypeCheck();
        }
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError,
            CannotAssignToConstError {
        for (ICmd cmd : commands) {
            cmd.executeInitCheck(globalProtected);
        }
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        for (ICmd cmd : commands) {
            cmd.addInstructionToCodeArray(localLocations, simulateOnly);
        }
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<commands>:\n";
        for (ICmd cmd : commands) {
            s += cmd.toString(subIndent);
        }

        return s;
    }
}
