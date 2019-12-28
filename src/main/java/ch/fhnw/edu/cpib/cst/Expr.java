package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1;

// expr ::= term1 exprNTS
public class Expr extends Production implements IExpr {
    protected final ITerm1 N_term1;
    protected final IExprNTS N_exprNTS;

    public Expr(final ITerm1 n_term1, final IExprNTS n_exprNTS) {
        N_term1 = n_term1;
        N_exprNTS = n_exprNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return N_exprNTS.toAbsSyntax(N_term1.toAbsSyntax());
    }
}
