package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IFlowModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.keywords.Flowmode;
import ch.fhnw.edu.cpib.scanner.Token;

// flowModeNTS ::= FLOWMODE
public class FlowModeNTS extends Production implements IFlowModeNTS {
    protected final Token T_flowMode;

    public FlowModeNTS(final Token t_flowMode) {
        T_flowMode = t_flowMode;
    }

    @Override
    public Flowmodes toAbsSyn() {
        return ((Flowmode)T_flowMode).getFlowmode();
    }
}
