package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm1NTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm2;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// term1NTS ::= RELOPR term2 term1NTS
public class Term1NTS extends Production implements ITerm1NTS {
    protected final Token T_relOpr;
    protected final ITerm2 N_term2;
    protected final ITerm1NTS N_term1NTS;

    public Term1NTS(final Token t_relOpr,
                    final ITerm2 n_term2,
                    final ITerm1NTS n_term1NTS) {
        T_relOpr = t_relOpr;
        N_term2 = n_term2;
        N_term1NTS = n_term1NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(IExpr expr) {
        ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr temp = new ch.fhnw.edu.cpib.absSynTree.RelExpr(((Operator)T_relOpr).getOperator(), expr, N_term2.toAbsSyn());
        return N_term1NTS.toAbsSyn(temp);
    }
}
