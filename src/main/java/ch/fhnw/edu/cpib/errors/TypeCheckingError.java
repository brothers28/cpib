package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public class TypeCheckingError extends Exception {
    private static final long serialVersionUID = 1L;

    public TypeCheckingError() {
    }

    public TypeCheckingError(String errorMessage) {
        super(errorMessage);
    }

    public TypeCheckingError(Types expected, Types found) {
        super(setupMessage(expected, found));
    }

    public TypeCheckingError(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeCheckingError(Types expected, Types found, Throwable cause) {
        super(setupMessage(expected, found), cause);
    }

    public TypeCheckingError(Throwable cause) {
        super(cause);
    }

    private static String setupMessage(Types expected, Types found) {
        return "Expected type [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
