package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AssignCmd extends AstNode implements ICmd {
    protected IExpr exprLeft;
    protected IExpr exprRight;

    public AssignCmd(IExpr exprLeft, IExpr exprRight) {
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
        exprLeft.setNamespaceInfo(this.localVarNamespace);
        exprRight.setNamespaceInfo(this.localVarNamespace);

    }

    @Override public void executeScopeCheck() throws NotDeclaredError, LRValError, InvalidParamCountError {
        exprLeft.executeScopeCheck();
        exprRight.executeScopeCheck();

        // Has to be LVALUE
        if (exprLeft.getLRValue() == LRValue.RVALUE)
            throw new LRValError(LRValue.LVALUE, exprLeft.getLRValue());
    }

    @Override public void executeTypeCheck() throws TypeCheckError, CastError {
        exprLeft.executeTypeCheck();
        exprRight.executeTypeCheck();

        // Check allowed types
        if (exprLeft.getType() != exprRight.getType()) //&& !isCastable(exprLeft.getType(), exprRight.getType()))
            throw new TypeCheckError(exprLeft.getType(), exprRight.getType());
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        // exprLeft can only be an Init-Factor
        InitFactor factor = (InitFactor) exprLeft;

        // Check if variable already initialized and a constant
        TypedIdent typedIdent = null;
        if (this.localVarNamespace.containsKey(factor.ident.getIdent()))
            typedIdent = this.localVarNamespace.get(factor.ident.getIdent());
        if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
            typedIdent = globalVarNamespace.get(factor.ident.getIdent());
        }
        if (typedIdent.getConst() && typedIdent.getInit())
            // Constant and already initialized
            throw new AssignToConstError(factor.ident);

        exprLeft.executeInitCheck(globalProtected);
        exprRight.executeInitCheck(globalProtected);
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {

        // Get exprLeft address and
        // add instruction depending on (casted) type
        InitFactor factor = (InitFactor) exprLeft;
        int address;
        if (!noExec) {
            if (globalVarAdresses.containsKey(factor.ident.getIdent())) {
                address = globalVarAdresses.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrAbs(address));
            } else if (localLocations.containsKey(factor.ident.getIdent())) {
                address = localLocations.get(factor.ident.getIdent());
                codeArray.put(codeArrayPointer, new IInstructions.LoadAddrRel(address));
            } else {
                throw new RuntimeException("No address found for " + factor.ident.getIdent() + " ?");
            }
        }
        codeArrayPointer++;

        // Deref
        TypedIdent variableIdent = null;
        if (globalVarNamespace.containsKey(factor.ident.getIdent())) {
            variableIdent = globalVarNamespace.get(factor.ident.getIdent());
        } else {
            variableIdent = localVarNamespace.get(factor.ident.getIdent());
        }
        if (variableIdent.getNeedToDeref()) {
            if (!noExec)
                codeArray.put(codeArrayPointer, new IInstructions.Deref());
            codeArrayPointer++;
        }

        // Get the value of exprRight
        exprRight.addToCodeArray(localLocations, noExec);

        // Copy value to new address
        if (!noExec)
            codeArray.put(codeArrayPointer, new IInstructions.Store());
        codeArrayPointer++;
    }

    @Override public String toString(String spaces) {
        // Set horizontal spaces
        String identifierIndendation = spaces;
        String argumentIndendation = spaces + " ";
        String lowerSpaces = spaces + "  ";

        // Get class
        String s = "";
        s += identifierIndendation + this.getClass().getName() + "\n";

        // Add arguments
        if (localVarNamespace != null)
            s += argumentIndendation + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";

        // Add elements
        s += argumentIndendation + "<exprLeft>:\n";
        s += exprLeft.toString(lowerSpaces);
        s += argumentIndendation + "<exprRight>:\n";
        s += exprRight.toString(lowerSpaces);

        return s;
    }
}
