package ch.fhnw.edu.cpib.scanner.symbols;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class AddOpr extends Operator {

    public AddOpr(Operators operator) {
        super(Terminals.ADDOPR, operator);
    }
}
