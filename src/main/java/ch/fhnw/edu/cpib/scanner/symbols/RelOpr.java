package ch.fhnw.edu.cpib.scanner.symbols;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class RelOpr extends Operator{

    public RelOpr(Operators operator) {
        super(Terminals.RELOPR, operator);
    }
}
