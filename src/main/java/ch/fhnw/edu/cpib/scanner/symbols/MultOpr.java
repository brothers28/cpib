package ch.fhnw.edu.cpib.scanner.symbols;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

public class MultOpr extends Operator{

    public MultOpr(Operators operator) {
        super(Terminals.MULTOPR, operator);
    }
}
