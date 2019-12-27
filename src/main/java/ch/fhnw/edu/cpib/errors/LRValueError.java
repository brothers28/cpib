package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;

public class LRValueError extends Exception {
    private static final long serialVersionUID = 1L;

    public LRValueError() {

    }

    public LRValueError(String errorMessage) {
        super(errorMessage);
    }

    public LRValueError(LRValue expected, LRValue found) {
        super(setupMessage(expected, found));
    }

    public LRValueError(String message, Throwable cause) {
        super(message, cause);
    }

    public LRValueError(LRValue expected, LRValue found, Throwable cause) {
        super(setupMessage(expected, found), cause);
    }

    public LRValueError(Throwable cause) {
        super(cause);
    }

    private static String setupMessage(LRValue expected, LRValue found) {
        return "Expected [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
