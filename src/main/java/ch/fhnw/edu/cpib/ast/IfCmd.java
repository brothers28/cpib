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

public class IfCmd extends AstNode implements ICmd {
    private IExpr expr;
    private CpsCmd ifCpsCmd;
    private CpsCmd elseCpsCmd;

    public IfCmd(IExpr expr, CpsCmd ifCpsCmd, CpsCmd elseCpsCmd) {
        this.expr = expr;
        this.ifCpsCmd = ifCpsCmd;
        this.elseCpsCmd = elseCpsCmd;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.saveNamespaceInfo(this.localVarNamespace);
        ifCpsCmd.saveNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
        elseCpsCmd.saveNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
    }

    @Override public void doScopeChecking() throws NotDeclaredError, LRValueError, InvalidParamCountError {
        expr.doScopeChecking();
        ifCpsCmd.doScopeChecking();
        elseCpsCmd.doScopeChecking();
    }

    @Override public void doTypeChecking() throws TypeCheckingError, CastError {
        expr.doTypeChecking();
        ifCpsCmd.doTypeChecking();
        elseCpsCmd.doTypeChecking();

        if (expr.getType() != Types.BOOL)
            throw new TypeCheckingError(Types.BOOL, expr.getType());
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        expr.doInitChecking(globalProtected);
        // set recursively all initialized variables also on the child-nodes to init
        for (TypeIdent ident : localVarNamespace.values()) {
            if (ident.getInit()) {
                ifCpsCmd.setInit(ident);
                elseCpsCmd.setInit(ident);
            }
        }
        // Do the init checking
        // Global variables cannot be initialized from now on
        ifCpsCmd.doInitChecking(true);
        elseCpsCmd.doInitChecking(true);
    }

    @Override public void addIInstrToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {
        // get the size of ifCpsCmd by simulating the add action
        int codeArrayPointerBefore = codeArrayPointer;

        ifCpsCmd.addIInstrToCodeArray(localLocations, true);
        int ifCpsCmdSize = codeArrayPointer - codeArrayPointerBefore + 1; // + 1 for unconditional jump after exprFalse

        // reset pointer
        codeArrayPointer = codeArrayPointerBefore;

        // get the size of elseCpsCmd
        elseCpsCmd.addIInstrToCodeArray(localLocations, true);
        int elseCpsCmdSize = codeArrayPointer - codeArrayPointerBefore;

        // reset pointer
        codeArrayPointer = codeArrayPointerBefore;

        // now really add the staff
        // add the boolean for the conditional check onto the stack
        expr.addIInstrToCodeArray(localLocations, simulateOnly);
        // now add the jump condition to see if we had to continue (true part) or to jump (false part)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + ifCpsCmdSize));
        codeArrayPointer++;
        // now add the true part
        ifCpsCmd.addIInstrToCodeArray(localLocations, simulateOnly);
        // now add the unconditional jump to jump after the false part (we already processed the true part ...)
        if (!simulateOnly)
            codeArray.put(codeArrayPointer, new IInstructions.UncondJump(codeArrayPointer + 1 + elseCpsCmdSize));
        codeArrayPointer++;
        // now add the false part
        elseCpsCmd.addIInstrToCodeArray(localLocations, simulateOnly);

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
        s += argumentIndent + "<ifCpsCmd>:\n";
        s += ifCpsCmd.toString(subIndent);
        s += argumentIndent + "<elseCpsCmd>:\n";
        s += elseCpsCmd.toString(subIndent);

        return s;
    }
}
