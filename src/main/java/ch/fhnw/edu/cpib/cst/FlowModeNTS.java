package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFlowModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Flowmode;

// flowModeNTS ::= FLOWMODE
public class FlowModeNTS extends Production implements IFlowModeNTS {
    protected final IToken T_flowMode;

    public FlowModeNTS(final IToken t_flowMode) {
        T_flowMode = t_flowMode;
    }

    @Override public Flowmodes toAbsSyn() {
        return ((Flowmode) T_flowMode).getFlowmode();
    }
}
