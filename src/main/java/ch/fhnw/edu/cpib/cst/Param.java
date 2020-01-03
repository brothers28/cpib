package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.*;

// param ::= flowModeNTS mechModeNTS changeModeNTS typedIdent
public class Param extends Production implements IParam {
    protected final IFlowModeNTS nts_flowModeNTS;
    protected final IMechModeNTS nts_mechModeNTS;
    protected final IChangeModeNTS nts_changeModeNTS;
    protected final ITypedIdent nts_typedIdent;

    public Param(final IFlowModeNTS nts_flowModeNTS, final IMechModeNTS nts_mechModeNTS,
            final IChangeModeNTS nts_changeModeNTS, final ITypedIdent nts_typedIdent) {
        this.nts_flowModeNTS = nts_flowModeNTS;
        this.nts_changeModeNTS = nts_changeModeNTS;
        this.nts_mechModeNTS = nts_mechModeNTS;
        this.nts_typedIdent = nts_typedIdent;
    }

    @Override public ch.fhnw.edu.cpib.ast.Param toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.Param(nts_flowModeNTS.toAbsSyntax(), nts_mechModeNTS.toAbsSyntax(),
                nts_changeModeNTS.toAbsSyntax(), nts_typedIdent.toAbsSyntax());
    }
}
