package ch.fhnw.edu.cpib.scanner.keywords;

import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Changemode extends Base {
    private final Changemodes changemode;

    public Changemode(Changemodes changemode) {
        super(Terminals.CHANGEMOD);
        this.changemode = changemode;
    }

    public Changemodes getChangemode() {
        return changemode;
    }

    public String toString() {
        return "(" + super.toString() + ", " + getChangemode().toString() + ")";
    }
}
