package ch.fhnw.edu.cpib.scanner.symbols;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class BoolOpr extends Operator {

    public BoolOpr(Operators operator) {
        super(Terminals.BOOLOPR, operator);
    }
}
