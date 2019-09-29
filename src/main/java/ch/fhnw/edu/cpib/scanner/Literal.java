package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Literal extends Base {
    private final int value;

    public Literal(Terminals terminal, int value) {
        super(terminal);
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "(" + super.toString() + ", " + getValue() + ")";
    }
}
