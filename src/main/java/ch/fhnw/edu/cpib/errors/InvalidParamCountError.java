package ch.fhnw.edu.cpib.errors;

public class InvalidParamCountError extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidParamCountError() {

    }

    public InvalidParamCountError(String errorMessage) {
        super(errorMessage);
    }

    public InvalidParamCountError(int expected, int found) {
        super(buildOutputMessage(expected, found));
    }

    public InvalidParamCountError(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamCountError(int expected, int found, Throwable cause) {
        super(buildOutputMessage(expected, found), cause);
    }

    public InvalidParamCountError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(int expected, int found) {
        return "Expected [" + expected + "] params but were [" + found + "] params";
    }
}
