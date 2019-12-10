package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICastOpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IMonadicOpr;

/**
 * @Author Hussein Farzi
 */

// factor ::= castOpr factor
public class FactorCastOpr extends Production implements IFactor {
    protected final ICastOpr N_castOpr;
    protected final IFactor N_mfactor;

    public FactorCastOpr(final ICastOpr n_castOpr,
                            final IFactor n_mfactor) {
        N_castOpr = n_castOpr;
        N_mfactor = n_mfactor;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.CastFactor(N_castOpr.toAbsSyn(), N_mfactor.toAbsSyn());
    }
}
