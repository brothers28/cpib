package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

/**
 * @Author Hussein Farzi
 */
public class GrammarError extends Exception {

    public GrammarError(String e) {
        super(e);
    }

    public GrammarError(Terminals expected, Terminals actual) {
        super("terminal expected: " + expected.toString() + ", terminal found: " + actual.toString());
    }
}
