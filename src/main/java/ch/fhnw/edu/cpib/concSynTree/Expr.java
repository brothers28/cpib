package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm1;

/**
 * @Author Hussein Farzi
 */

// expr ::= term1 exprNTS
public class Expr extends Production implements IExpr {
    protected final ITerm1 N_term1;
    protected final IExprNTS N_exprNTS;

    public Expr(final ITerm1 n_term1,
                final IExprNTS n_exprNTS) {
        N_term1 = n_term1;
        N_exprNTS = n_exprNTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn() {
        return N_exprNTS.toAbsSyn(N_term1.toAbsSyn());
    }
}
