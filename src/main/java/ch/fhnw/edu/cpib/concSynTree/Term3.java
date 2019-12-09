package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm3;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm3NTS;

/**
 * @Author Hussein Farzi
 */

// term3 ::= factor term3NTS
public class Term3 extends Production implements ITerm3 {
    protected final IFactor N_factor;
    protected final ITerm3NTS N_term3NTS;

    public Term3(final IFactor n_factor,
                 final ITerm3NTS n_term3NTS) {
        N_factor = n_factor;
        N_term3NTS = n_term3NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn() {
        return N_term3NTS.toAbsSyn(N_factor.toAbsSyn());
    }}
