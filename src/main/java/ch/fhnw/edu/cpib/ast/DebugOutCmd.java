package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class DebugOutCmd extends AstNode implements ICmd {
    private IExpr expr;

    public DebugOutCmd(IExpr expr) {
        this.expr = expr;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyGloballyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        expr.saveNamespaceInfo(this.localVarNamespace);
    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        expr.executeScopeCheck();
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        expr.executeTypeCheck();
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            AssignToConstError {
        expr.executeInitCheck(globalProtected);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        expr.addToCodeArray(localLocations, simulateOnly);

        String indicator;
        if (expr instanceof InitFactor) {
            indicator = ((InitFactor) expr).ident.getIdent();
        } else {
            indicator = "<anonymous>";
        }

        if (!simulateOnly) {
            if (expr.getType() == Types.BOOL) {
                codeArray.put(codeArrayPointer, new IInstructions.OutputBool(indicator));
            } else if (expr.getType() == Types.INT32) {
                codeArray.put(codeArrayPointer, new IInstructions.OutputInt(indicator));
            } else if (expr.getType() == Types.NAT32) {
                codeArray.put(codeArrayPointer, new IInstructions.OutputNat(indicator));
            } else {
                throw new RuntimeException("Unknown Type!");
            }
        }
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

        return s;
    }
}
