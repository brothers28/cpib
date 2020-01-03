package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.Ident;

public class NotInitializedError extends Exception {
    private static final long serialVersionUID = 1L;

    public NotInitializedError() {

    }

    public NotInitializedError(String errorMessage) {
        super(errorMessage);
    }

    public NotInitializedError(Ident ident) {
        super(buildOutputMessage(ident));
    }

    public NotInitializedError(String message, Throwable cause) {
        super(message, cause);
    }

    public NotInitializedError(Ident ident, Throwable cause) {
        super(buildOutputMessage(ident), cause);
    }

    public NotInitializedError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(Ident ident) {
        return "Identifier [" + ident.getIdent() + "] not initialized.";
    }
}
