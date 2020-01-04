package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Literal;
import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.ICodeArray.CodeTooSmallError;
import ch.fhnw.edu.cpib.vm.IInstructions;

import java.util.HashMap;
import java.util.stream.Collectors;

public class LiteralFactor extends AstNode implements IFactor {
    protected Literal literal;
    protected Types castType;

    public LiteralFactor(Literal literal) {
        this.literal = literal;
    }

    @Override public void setNamespaceInfo(HashMap<String, TypedIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void executeScopeCheck() throws NotDeclaredError {
    }

    @Override public void executeTypeCheck() throws TypeCheckError {
        //
    }

    @Override public void executeInitCheck(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, AssignToConstError {
        //
    }

    @Override public void executeTypeCast(Types type) {
        if (type != null) {
            this.castType = type;
        }
        literal.doTypeCasting(type);
    }

    @Override public LRValue getLRValue() {
        return LRValue.RVALUE;
    }

    @Override public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        return literal.getType();
    }

    @Override public void addToCodeArray(HashMap<String, Integer> localLocations, boolean noExec)
            throws CodeTooSmallError {

        // Add to top of stack
        if (!noExec) {
            try {
                if (literal.getType() == Types.BOOL) {
                    codeArray.put(codeArrayPointer, new IInstructions.LoadImBool(literal.getBoolValue()));
                } else if (literal.getType() == Types.INT32) {
                    codeArray.put(codeArrayPointer, new IInstructions.LoadImInt(literal.getIntValue()));
                } else if (literal.getType() == Types.NAT32) {
                    codeArray.put(codeArrayPointer, new IInstructions.LoadImNat(literal.getNatValue()));
                } else {
                    throw new RuntimeException("Unknown type!");
                }
            } catch (Exception ex) {

            }
        }
        codeArrayPointer++;
    }

    @Override public String toString(String indent) {
        String nameIndent = indent;
        String argumentIndent = indent + " ";
        String s = "";
        s += nameIndent + this.getClass().getName() + "\n";
        if (localVarNamespace != null)
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream().map(Object::toString)
                    .collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<literal>: " + literal.toString() + "\n";

        return s;
    }
}
