package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// exprNTS ::= BOOLOPR term1 exprNTS
public class ExprNTS extends Production implements IExprNTS {
    protected final IToken T_boolOpr;
    protected final ITerm1 N_term1;
    protected final IExprNTS N_exprNTS;

    public ExprNTS(final IToken t_boolOpr, final ITerm1 n_term1, final IExprNTS n_exprNTS) {
        T_boolOpr = t_boolOpr;
        N_term1 = n_term1;
        N_exprNTS = n_exprNTS;
    }

    @Override public IExpr toAbsSyn(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.BoolExpr(
                ((Operator) T_boolOpr).getOperator(), expr, N_term1.toAbsSyn());
        return N_exprNTS.toAbsSyn(temp);
    }
}
