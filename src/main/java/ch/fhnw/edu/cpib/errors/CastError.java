package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public class CastError extends Exception {
    private static final long serialVersionUID = 1L;

    public CastError() {

    }

    public CastError(String errorMessage) {
        super(errorMessage);
    }

    public CastError(Types expected, Types found) {
        super(buildOutputMessage(expected, found));
    }

    public CastError(String message, Throwable cause) {
        super(message, cause);
    }

    public CastError(Types expected, Types found, Throwable cause) {
        super(buildOutputMessage(expected, found), cause);
    }

    public CastError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(Types expected, Types found) {
        return "Expected type casteable to [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
