package ch.fhnw.edu.cpib.scanner.keywords;

import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public class Type extends Base {
    private final Types type;

    public Type(Types type) {
        super(Terminals.TYPE);
        this.type = type;
    }

    public Types getType(){
        return type;
    }

    public String toString(){
        return "(" + super.toString() + ", " + getType().toString() + ")";
    }
}
