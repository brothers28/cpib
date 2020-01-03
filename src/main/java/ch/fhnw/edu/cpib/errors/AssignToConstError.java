package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.Ident;

public class AssignToConstError extends Exception {
    private static final long serialVersionUID = 1L;

    public AssignToConstError() {

    }

    public AssignToConstError(String errorMessage) {
        super(errorMessage);
    }

    public AssignToConstError(Ident ident) {
        super(buildOutputMessage(ident));
    }

    public AssignToConstError(String message, Throwable cause) {
        super(message, cause);
    }

    public AssignToConstError(Ident ident, Throwable cause) {
        super(buildOutputMessage(ident), cause);
    }

    public AssignToConstError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(Ident ident) {
        return "Const [" + ident.getIdent() + "] is already initialized!";
    }
}
