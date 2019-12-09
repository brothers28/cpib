package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm3;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// term2NTS ::= ADDOPR term3 term2NTS
public class Term2NTS extends Production implements ITerm2NTS {
    protected final Token T_addOpr;
    protected final ITerm3 N_term3;
    protected final ITerm2NTS N_term2NTS;

    public Term2NTS(final Token t_addOpr,
                    final ITerm3 n_term3,
                    final ITerm2NTS n_term2NTS) {
        T_addOpr = t_addOpr;
        N_term3 = n_term3;
        N_term2NTS = n_term2NTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(IExpr expr) {
        ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr temp = new ch.fhnw.edu.cpib.absSynTree.AddExpr(((Operator)T_addOpr).getOperator(), expr, N_term3.toAbsSyn());
        return N_term2NTS.toAbsSyn(temp);
    }}
