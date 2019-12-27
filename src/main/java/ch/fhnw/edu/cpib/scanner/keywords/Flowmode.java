package ch.fhnw.edu.cpib.scanner.keywords;

import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Flowmode extends Base {
    private final Flowmodes flowmode;

    public Flowmode(Flowmodes flowmode) {
        super(Terminals.FLOWMODE);
        this.flowmode = flowmode;
    }

    public Flowmodes getFlowmode() {
        return flowmode;
    }

    public String toString() {
        return "(" + super.toString() + ", " + getFlowmode().toString() + ")";
    }
}
