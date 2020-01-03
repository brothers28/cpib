package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public class TypeCheckError extends Exception {
    private static final long serialVersionUID = 1L;

    public TypeCheckError() {

    }

    public TypeCheckError(String errorMessage) {
        super(errorMessage);
    }

    public TypeCheckError(Types expected, Types found) {
        super(buildOutputMessage(expected, found));
    }

    public TypeCheckError(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeCheckError(Types expected, Types found, Throwable cause) {
        super(buildOutputMessage(expected, found), cause);
    }

    public TypeCheckError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(Types expected, Types found) {
        return "Expected type [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
