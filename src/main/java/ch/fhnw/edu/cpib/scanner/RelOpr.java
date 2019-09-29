package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class RelOpr extends Operator{

    public RelOpr(Terminals terminal, Operators operator) {
        super(terminal, operator);
    }
}
