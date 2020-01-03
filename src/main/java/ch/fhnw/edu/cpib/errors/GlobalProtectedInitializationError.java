package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.Ident;

public class GlobalProtectedInitializationError extends Exception {
    private static final long serialVersionUID = 1L;

    public GlobalProtectedInitializationError() {

    }

    public GlobalProtectedInitializationError(String errorMessage) {
        super(errorMessage);
    }

    public GlobalProtectedInitializationError(Ident ident) {
        super(setupMessage(ident));
    }

    public GlobalProtectedInitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalProtectedInitializationError(Ident ident, Throwable cause) {
        super(setupMessage(ident), cause);
    }

    public GlobalProtectedInitializationError(Throwable cause) {
        super(cause);
    }

    private static String setupMessage(Ident ident) {
        return "Identifier [" + ident.getIdent() + "] is global and therefore cannot be initialized in protected scope.";
    }
}
