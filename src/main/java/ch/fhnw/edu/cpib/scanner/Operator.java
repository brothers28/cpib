package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Operator extends Base {
    private final Operators operator;

    public Operator(Terminals terminal, Operators operator) {
        super(terminal);
        this.operator = operator;
    }

    public Operators getOperator(){
        return operator;
    }

    public String toString(){
        return "(" + super.toString() + ", " + getOperator().toString() + ")";
    }
}
