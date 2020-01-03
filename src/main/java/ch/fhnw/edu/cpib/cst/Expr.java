package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1;

// expr ::= term1 exprNTS
public class Expr extends Production implements IExpr {
    protected final ITerm1 nts_term1;
    protected final IExprNTS nts_exprNTS;

    public Expr(final ITerm1 nts_term1, final IExprNTS nts_exprNTS) {
        this.nts_term1 = nts_term1;
        this.nts_exprNTS = nts_exprNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return nts_exprNTS.toAbsSyntax(nts_term1.toAbsSyntax());
    }
}
