package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprListNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// exprListNTS ::= COMMA expr exprListNTS
public class ExprListNTS extends Production implements IExprListNTS {
    protected final Token T_comma;
    protected final IExpr N_expr;
    protected final IExprListNTS N_exprListNTS;

    public ExprListNTS(final Token t_comma,
                       final IExpr n_expr,
                       final IExprListNTS n_exprListNTS) {
        T_comma = t_comma;
        N_expr = n_expr;
        N_exprListNTS = n_exprListNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> temp) {
        temp.add(N_expr.toAbsSyn());
        return N_exprListNTS.toAbsSyn(temp);
    }
}
