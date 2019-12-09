package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprList;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;

/**
 * @Author Hussein Farzi
 */

// factorNTS ::= exprList
public class FactorNTSExprList extends Production implements IFactorNTS {

   protected final IExprList N_exprList;

    public FactorNTSExprList(final IExprList n_exprList) {
        N_exprList = n_exprList;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn(Ident ident) {
        return new ch.fhnw.edu.cpib.absSynTree.FunCallFactor(ident, N_exprList.toAbsSyn());
    }
}
