package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;

public class LRValError extends Exception {
    private static final long serialVersionUID = 1L;

    public LRValError() {

    }

    public LRValError(String errorMessage) {
        super(errorMessage);
    }

    public LRValError(LRValue expected, LRValue found) {
        super(buildOutputMessage(expected, found));
    }

    public LRValError(String message, Throwable cause) {
        super(message, cause);
    }

    public LRValError(LRValue expected, LRValue found, Throwable cause) {
        super(buildOutputMessage(expected, found), cause);
    }

    public LRValError(Throwable cause) {
        super(cause);
    }

    private static String buildOutputMessage(LRValue expected, LRValue found) {
        return "Expected [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
