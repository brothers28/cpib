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
    protected IExpr expr;
    protected CpsCmd ifCpsCmd;
    protected CpsCmd elseCpsCmd;

    public IfCmd(IExpr expr, CpsCmd ifCpsCmd, CpsCmd elseCpsCmd) {
        this.expr = expr;
        this.ifCpsCmd = ifCpsCmd;
        this.elseCpsCmd = elseCpsCmd;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.setNamespaceInfo(this.localVarNamespace);
        ifCpsCmd.setNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
        elseCpsCmd.setNamespaceInfo(DataStructureHelper.deepCopy(this.localVarNamespace));
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        expr.executeScopeCheck();
        ifCpsCmd.executeScopeCheck();
        elseCpsCmd.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        expr.executeTypeCheck();
        ifCpsCmd.executeTypeCheck();
        elseCpsCmd.executeTypeCheck();

        if (expr.getType() != Types.BOOL)
            throw new TypeCheckError(Types.BOOL, expr.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        expr.executeInitCheck(globalProtected);

        // Set initialized variables
        for (TypedIdent ident : localVarNamespace.values()) {
            if (ident.getInit()) {
                ifCpsCmd.setInit(ident);
                elseCpsCmd.setInit(ident);
            }
        }

        // Prohibited to initialize global variables
        ifCpsCmd.executeInitCheck(true);
        elseCpsCmd.executeInitCheck(true);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {
        // Get size of if cmd
        // NoExec = true!
        int pointerBefore = codeArrayPointer;
        ifCpsCmd.addToCodeArray(localLocations, true);
        int ifCpsCmdSize = codeArrayPointer - pointerBefore + 1; // + 1 for unconditional jump after exprFalse

        // Reset pointer
        codeArrayPointer = pointerBefore;

        // Get size of if cmd
        // NoExec = true!
        elseCpsCmd.addToCodeArray(localLocations, true);
        int elseCpsCmdSize = codeArrayPointer - pointerBefore;

        // Reset pointer
        codeArrayPointer = pointerBefore;

        // Execute
        // Add boolean expression to sack
        expr.addToCodeArray(localLocations, noExec);
        // Jump condition for true part and false part
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.CondJump(codeArrayPointer + 1 + ifCpsCmdSize));
        codeArrayPointer++;
        // True part
        ifCpsCmd.addToCodeArray(localLocations, noExec);
        // Overjump false part
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.UncondJump(codeArrayPointer + 1 + elseCpsCmdSize));
        codeArrayPointer++;
        // False part
        elseCpsCmd.addToCodeArray(localLocations, noExec);

    }

    @Override public String toString(String indent) {
        // Set horizontal spaces
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String subIndent = indent + "  ";

        // Get class
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndent + "<expr>:\n";
        s += expr.toString(subIndent);
        s += argumentIndent + "<ifCpsCmd>:\n";
        s += ifCpsCmd.toString(subIndent);
        s += argumentIndent + "<elseCpsCmd>:\n";
        s += elseCpsCmd.toString(subIndent);

        return s;
    }
}
