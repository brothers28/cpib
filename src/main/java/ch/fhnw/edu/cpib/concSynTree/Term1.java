package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm1;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm1NTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm2;

/**
 * @Author Hussein Farzi
 */

// term1 ::= term2 term1NTS
public class Term1 extends Production implements ITerm1 {
    protected final ITerm2 N_term2;
    protected final ITerm1NTS N_term1NTS;

    public Term1(final ITerm2 n_term2,
                 final ITerm1NTS n_term1NTS) {
        N_term2 = n_term2;
        N_term1NTS = n_term1NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn() {
        return N_term1NTS.toAbsSyn(N_term2.toAbsSyn());
    }
}
