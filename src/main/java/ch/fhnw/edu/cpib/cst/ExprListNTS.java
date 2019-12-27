package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// exprListNTS ::= COMMA expr exprListNTS
public class ExprListNTS extends Production implements IExprListNTS {
    protected final IToken T_comma;
    protected final IExpr N_expr;
    protected final IExprListNTS N_exprListNTS;

    public ExprListNTS(final IToken t_comma, final IExpr n_expr, final IExprListNTS n_exprListNTS) {
        T_comma = t_comma;
        N_expr = n_expr;
        N_exprListNTS = n_exprListNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> toAbsSyn(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> temp) {
        temp.add(N_expr.toAbsSyn());
        return N_exprListNTS.toAbsSyn(temp);
    }
}
