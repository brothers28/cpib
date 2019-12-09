package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprListLparenNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprListNTS;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// exprListLparenNTS ::= expr exprListNTS
public class ExprListLParenNTS extends Production implements IExprListLparenNTS {
    protected final IExpr N_expr;
    protected final IExprListNTS N_exprListNTS;

    public ExprListLParenNTS(final IExpr n_expr,
                             final IExprListNTS n_exprListNTS) {
        N_expr = n_expr;
        N_exprListNTS = n_exprListNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> toAbsSyn() {
        ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> temp = new ArrayList<>();
        temp.add(N_expr.toAbsSyn());
        return N_exprListNTS.toAbsSyn(temp);
    }
}
