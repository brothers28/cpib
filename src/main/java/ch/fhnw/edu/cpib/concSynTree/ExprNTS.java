package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITerm1;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// exprNTS ::= BOOLOPR term1 exprNTS
public class ExprNTS extends Production implements IExprNTS {
    protected final Token T_boolOpr;
    protected final ITerm1 N_term1;
    protected final IExprNTS N_exprNTS;

    public ExprNTS(final Token t_boolOpr,
                   final ITerm1 n_term1,
                   final IExprNTS n_exprNTS) {
        T_boolOpr = t_boolOpr;
        N_term1 = n_term1;
        N_exprNTS = n_exprNTS;
    }

    @Override
    public IExpr toAbsSyn(IExpr expr) {
        ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr temp = new ch.fhnw.edu.cpib.absSynTree.BoolExpr(((Operator)T_boolOpr).getOperator(), expr, N_term1.toAbsSyn());
        return N_exprNTS.toAbsSyn(temp);
    }
}
