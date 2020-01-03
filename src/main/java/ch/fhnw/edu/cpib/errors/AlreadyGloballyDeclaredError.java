package ch.fhnw.edu.cpib.errors;

public class AlreadyGloballyDeclaredError extends Exception {
    private static final long serialVersionUID = 1L;

    public AlreadyGloballyDeclaredError() {

    }

    public AlreadyGloballyDeclaredError(String errorMessage) {
        super(buildOutputMessage(errorMessage));
    }

    public AlreadyGloballyDeclaredError(String message, Throwable cause) {
        super(buildOutputMessage(message), cause);
    }

    public AlreadyGloballyDeclaredError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(String string) {
        return "Name already globally declared [" + string + "]";
    }
}
