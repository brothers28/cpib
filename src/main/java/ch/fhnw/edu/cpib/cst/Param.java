package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.*;

// param ::= flowModeNTS mechModeNTS changeModeNTS typedIdent
public class Param extends Production implements IParam {
    protected final IFlowModeNTS N_flowModeNTS;
    protected final IMechModeNTS N_mechModeNTS;
    protected final IChangeModeNTS N_changeModeNTS;
    protected final ITypedIdent N_typedIdent;

    public Param(final IFlowModeNTS N_flowModeNTS, final IMechModeNTS N_mechModeNTS,
            final IChangeModeNTS N_changeModeNTS, final ITypedIdent N_typedIdent) {
        this.N_flowModeNTS = N_flowModeNTS;
        this.N_changeModeNTS = N_changeModeNTS;
        this.N_mechModeNTS = N_mechModeNTS;
        this.N_typedIdent = N_typedIdent;
    }

    @Override public ch.fhnw.edu.cpib.ast.Param toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.Param(N_flowModeNTS.toAbsSyn(), N_mechModeNTS.toAbsSyn(),
                N_changeModeNTS.toAbySyn(), N_typedIdent.toAbsSyn());
    }
}
