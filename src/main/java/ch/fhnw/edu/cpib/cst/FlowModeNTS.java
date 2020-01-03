package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFlowModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Flowmode;

// flowModeNTS ::= FLOWMODE
public class FlowModeNTS extends Production implements IFlowModeNTS {
    private IToken ts_flowMode;

    public FlowModeNTS(IToken ts_flowMode) {
        this.ts_flowMode = ts_flowMode;
    }

    @Override public Flowmodes toAbsSyntax() {
        return ((Flowmode) ts_flowMode).getFlowmode();
    }
}
