package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm3NTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// term3NTS ::= MULTOPR factor term3NTS
public class Term3NTS extends Production implements ITerm3NTS {
    protected final Token T_multOpr;
    protected final IFactor N_factor;
    protected final ITerm3NTS N_term3NTS;

    public Term3NTS(final Token t_multOpr,
                    final IFactor n_factor,
                    final ITerm3NTS n_term3NTS) {
        T_multOpr = t_multOpr;
        N_factor = n_factor;
        N_term3NTS = n_term3NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(IExpr expr) {
        ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr temp = new ch.fhnw.edu.cpib.absSynTree.MultExpr(((Operator)T_multOpr).getOperator(), expr, N_factor.toAbsSyn());
        return N_term3NTS.toAbsSyn(temp);
    }
}
