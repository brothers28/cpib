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
        super(setupMessage(expected, found));
    }

    public LRValError(String message, Throwable cause) {
        super(message, cause);
    }

    public LRValError(LRValue expected, LRValue found, Throwable cause) {
        super(setupMessage(expected, found), cause);
    }

    public LRValError(Throwable cause) {
        super(cause);
    }

    private static String setupMessage(LRValue expected, LRValue found) {
        return "Expected [" + expected.name() + "] but found [" + found.name() + "]";
    }
}
