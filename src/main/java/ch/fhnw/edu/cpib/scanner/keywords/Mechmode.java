package ch.fhnw.edu.cpib.scanner.keywords;

import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Mechmode extends Base {
    private final Mechmodes mechmode;

    public Mechmode(Mechmodes mechmode) {
        super(Terminals.MECHMODE);
        this.mechmode = mechmode;
    }

    public Mechmodes getMechmode() {
        return mechmode;
    }

    public String toString() {
        return "(" + super.toString() + ", " + getMechmode().toString() + ")";
    }
}
