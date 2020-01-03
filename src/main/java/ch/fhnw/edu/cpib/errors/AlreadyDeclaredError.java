package ch.fhnw.edu.cpib.errors;

public class AlreadyDeclaredError extends Exception {
    private static final long serialVersionUID = 1L;

    public AlreadyDeclaredError() {

    }

    public AlreadyDeclaredError(String errorMessage) {
        super(buildOutputMessage(errorMessage));
    }

    public AlreadyDeclaredError(String message, Throwable cause) {
        super(buildOutputMessage(message), cause);
    }

    public AlreadyDeclaredError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(String string) {
        return "Name already declared [" + string + "]";
    }
}
