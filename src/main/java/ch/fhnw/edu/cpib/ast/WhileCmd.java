package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.parser.util.DataStructureHelper;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class WhileCmd extends AstNode implements ICmd {
    private IExpr expr;
    private CpsCmd cpsCmd;

    public WhileCmd(IExpr expr, CpsCmd cpsCmd) {
        this.expr = expr;
        this.cpsCmd = cpsCmd;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.saveNamespaceInfo(this.localVarNamespace);
        // inner while body with deepCopy from localStorage
        cpsCmd.saveNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        expr.executeScopeCheck();
        cpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckingError, CastError {
        expr.executeTypeCheck();
        cpsCmd.executeTypeCheck();

        // Check allowed types
        if (expr.getType() != Types.BOOL)
            throw new TypeCheckingError(Types.BOOL, expr.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        expr.executeInitCheck(globalProtected);
        // set recursively all initialized variables also on the child-nodes to init
        for (TypeIdent ident : localVarNamespace.values()) {
            if (ident.getInit()) {
                cpsCmd.setInit(ident);
            }
        }
        // Do the init checking
        // Global variables cannot be initialized from now on
        cpsCmd.executeInitCheck(true);
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // get the size of cpsCmd by simulating the add action
        int codeArrayPointerBefore = codeArrayPointer;

        cpsCmd.addInstructionToCodeArray(localLocations, true);
        int cpsCmdSize = codeArrayPointer - codeArrayPointerBefore + 1; // + 1 for unconditional jump after exprFalse

        // reset pointer
        codeArrayPointer = codeArrayPointerBefore;

        // save the start of the while loop (where we save the boolean onto the stack and to the jump afterwards)
        int loopStartAddress = codeArrayPointer;

        // add the boolean for the conditional check onto the stack
        expr.addInstructionToCodeArray(localLocations, simulateOnly);
        // now add the jump condition to see if we had to continue (loop part) or to jump (after the loop part)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + cpsCmdSize));
        codeArrayPointer++;
        // now add the loop part
        cpsCmd.addInstructionToCodeArray(localLocations, simulateOnly);
        // now add the unconditional jump to jump back to the jump condition (we already processed the loop part ...)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.UncondJump(loopStartAddress));
        codeArrayPointer++;
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
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);
        s += argumentIndent + "<cpsCmd>:\n";
        s += cpsCmd.toString(subIndent);

        return s;
    }
}
