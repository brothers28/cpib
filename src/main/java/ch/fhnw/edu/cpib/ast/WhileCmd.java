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
    protected IExpr expr;
    protected CpsCmd cpsCmd;

    public WhileCmd(IExpr expr, CpsCmd cpsCmd) {
        this.expr = expr;
        this.cpsCmd = cpsCmd;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.setNamespaceInfo(this.localVarNamespace);
        // inner while body with deepCopy from localStorage
        cpsCmd.setNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        expr.executeScopeCheck();
        cpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        expr.executeTypeCheck();
        cpsCmd.executeTypeCheck();

        // Check allowed types
        if (expr.getType() != Types.BOOL)
            throw new TypeCheckError(Types.BOOL, expr.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        expr.executeInitCheck(globalProtected);

        // Set initialized variables
        for (TypedIdent ident : localVarNamespace.values()) {
            if (ident.getInit()) {
                cpsCmd.setInit(ident);
            }
        }

        // Prohibited to initialize global variables
        cpsCmd.executeInitCheck(true);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        // Get size of cmd
        int pointerBefore = codeArrayPointer;
        // NoExec = true
        cpsCmd.addToCodeArray(localLocations, true);
        int cpsCmdSize = codeArrayPointer - pointerBefore + 1;
        // Reset pointer
        codeArrayPointer = pointerBefore;

        // Save the start address of while loop
        int startAddress = codeArrayPointer;
        // Check condition
        expr.addToCodeArray(localLocations, noExec);
        // Jump condition to check if we have to continue or to jump to the end of the loop
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + cpsCmdSize));
        codeArrayPointer++;
        //  Loop part
        cpsCmd.addToCodeArray(localLocations, noExec);
        // Jump back to condition
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.UncondJump(startAddress));
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);
        s += argumentIndent + "<cpsCmd>:\n";
        s += cpsCmd.toString(subIndent);

        return s;
    }
}
