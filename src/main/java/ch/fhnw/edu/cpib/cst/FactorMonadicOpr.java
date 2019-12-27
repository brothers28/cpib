package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;

// factor ::= monadicOpr factor
public class FactorMonadicOpr extends Production implements IFactor {
    protected final IMonadicOpr N_monadicOpr;
    protected final IFactor N_mfactor;

    public FactorMonadicOpr(final IMonadicOpr n_monadicOpr, final IFactor n_mfactor) {
        N_monadicOpr = n_monadicOpr;
        N_mfactor = n_mfactor;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.MonadicFactor(N_monadicOpr.toAbsSyn(), N_mfactor.toAbsSyn());
    }
}
