package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import jdk.jshell.spi.ExecutionControl;

public class Literal extends Base {
    private final boolean isBoolean;
    private final int value;

    public Literal(Terminals terminal) {
        super(terminal);
        this.isBoolean = true;
        this.value = 0; // Default, not used
    }

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

    public long getValue(){
        return value;
    }

    public Types getType(){
        return isBoolean ? Types.BOOL : Types.INT64;
    }

    public boolean getBoolValue() throws ExecutionControl.NotImplementedException {
        if(!isBoolean)
            throw new ExecutionControl.NotImplementedException("Gagu");

        return value > 0 ? true : false;
    }

    public int getIntValue() throws ExecutionControl.NotImplementedException {
        if(isBoolean)
            throw new ExecutionControl.NotImplementedException("Gagu 2");

        return value;
    }

    @Override
    public String toString(){
        return "(" + super.toString() + ", " + (this.isBoolean ? "BoolVal" : "") + getValue() + ")";
    }
}
