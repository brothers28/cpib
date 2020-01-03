package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class GrammarError extends Exception {

    public GrammarError(String e) {
        super(e);
    }

    public GrammarError(Terminals expected, Terminals actual) {
        super("Terminal expected: " + expected.toString() + ", Terminal found: " + actual.toString());
    }
}
