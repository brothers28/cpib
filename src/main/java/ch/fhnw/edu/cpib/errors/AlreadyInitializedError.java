package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.Ident;

public class AlreadyInitializedError extends Exception {
    private static final long serialVersionUID = 1L;

    public AlreadyInitializedError() {

    }

    public AlreadyInitializedError(String errorMessage) {
        super(errorMessage);
    }

    public AlreadyInitializedError(Ident ident) {
        super(buildOutputMessage(ident));
    }

    public AlreadyInitializedError(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyInitializedError(Ident ident, Throwable cause) {
        super(buildOutputMessage(ident), cause);
    }

    public AlreadyInitializedError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(Ident ident) {
        return "Identifier [" + ident.getIdent() + "] is already initialized.";
    }
}
