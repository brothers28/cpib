package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm2;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm3;

/**
 * @Author Hussein Farzi
 */

// term2 ::= term3 term2NTS
public class Term2 extends Production implements ITerm2 {
    protected final ITerm3 N_term3;
    protected final ITerm2NTS N_term2NTS;

    public Term2(final ITerm3 n_term3,
                 final ITerm2NTS n_term2NTS) {
        N_term3 = n_term3;
        N_term2NTS = n_term2NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn() {
        return N_term2NTS.toAbsSyn(N_term3.toAbsSyn());
    }}