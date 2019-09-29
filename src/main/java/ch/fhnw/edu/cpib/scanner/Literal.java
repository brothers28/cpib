package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Literal extends Base {
    private final boolean isBoolean;
    private final int value;

    public Literal(Terminals terminal, int value) {
        super(terminal);
        this.isBoolean = false;
        this.value = value;
    }

    public Literal(Terminals terminal, int value, boolean isBoolean){
        super(terminal);
        this.isBoolean = isBoolean;
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "(" + super.toString() + ", " + (this.isBoolean ? "BoolVal" : "") + getValue() + ")";
    }
}
