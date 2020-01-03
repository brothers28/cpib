package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.*;

// param ::= flowModeNTS mechModeNTS changeModeNTS typedIdent
public class Param extends Production implements IParam {
    private IFlowModeNTS nts_flowModeNTS;
    private IMechModeNTS nts_mechModeNTS;
    private IChangeModeNTS nts_changeModeNTS;
    private ITypedIdent nts_typedIdent;

    public Param(IFlowModeNTS nts_flowModeNTS, IMechModeNTS nts_mechModeNTS,
            IChangeModeNTS nts_changeModeNTS, ITypedIdent nts_typedIdent) {
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
