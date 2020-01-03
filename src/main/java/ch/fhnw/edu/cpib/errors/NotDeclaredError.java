package ch.fhnw.edu.cpib.errors;

public class NotDeclaredError extends Exception {
    private static final long serialVersionUID = 1L;

    public NotDeclaredError() {

    }

    public NotDeclaredError(String errorMessage) {
        super(buildOutputMessage(errorMessage));
    }

    public NotDeclaredError(String message, Throwable cause) {
        super(buildOutputMessage(message), cause);
    }

    public NotDeclaredError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(String string) {
        return "Name [" + string + "] not declared";
    }
}
