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
    private Literal literal;
    private Types castType;

    public LiteralFactor(Literal literal) {
        this.literal = literal;
    }

    @Override public void saveNamespaceInfo(HashMap<String, TypeIdent> localStoresNamespace)
            throws AlreadyDeclaredError, AlreadyInitializedError {
        this.localVarNamespace = localStoresNamespace;
    }

    @Override public void doScopeChecking() throws NotDeclaredError {
    }

    @Override public void doTypeCasting(Types type) {
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

    @Override public void doTypeChecking() throws TypeCheckingError {
        //
    }

    @Override public void doInitChecking(boolean globalProtected)
            throws NotInitializedError, AlreadyInitializedError, GlobalProtectedInitializationError,
            CannotAssignToConstError {
        //
    }

    @Override public void addInstructionToCodeArray(HashMap<String, Integer> localLocations, boolean simulateOnly)
            throws CodeTooSmallError {

        // Add the value on top of stack
        if (!simulateOnly) {
            try {
                if(castType == null) {
                    if (literal.getType() == Types.BOOL) {
                        codeArray.put(codeArrayPointer, new IInstructions.LoadImBool(literal.getBoolValue()));
                    } else if (literal.getType() == Types.INT64) {
                        codeArray.put(codeArrayPointer, new IInstructions.LoadImInt(literal.getIntValue()));
                    } else if (literal.getType() == Types.NAT64) {
                        codeArray.put(codeArrayPointer, new IInstructions.LoadImNat(literal.getNatValue()));
                    } else {
                        throw new RuntimeException("Unknown type!");
                    }
                } else if (castType == Types.BOOL) {
                    codeArray.put(codeArrayPointer, new IInstructions.LoadImBool(literal.getBoolValue()));
                } else if (castType == Types.INT64) {
                    codeArray.put(codeArrayPointer, new IInstructions.LoadImInt(literal.getIntValue()));
                } else if (castType == Types.NAT64) {
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
            s += argumentIndent + "[localStoresNamespace]: " + localVarNamespace.keySet().stream()
                    .map(Object::toString).collect(Collectors.joining(",")) + "\n";
        s += argumentIndent + "<literal>: " + literal.toString() + "\n";

        return s;
    }
}
