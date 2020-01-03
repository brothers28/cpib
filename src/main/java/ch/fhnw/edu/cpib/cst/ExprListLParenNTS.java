package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListLparenNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListNTS;

import java.util.ArrayList;

// exprListLparenNTS ::= expr exprListNTS
public class ExprListLParenNTS extends Production implements IExprListLparenNTS {
    protected final IExpr nts_expr;
    protected final IExprListNTS nts_exprListNTS;

    public ExprListLParenNTS(final IExpr nts_expr, final IExprListNTS nts_exprListNTS) {
        this.nts_expr = nts_expr;
        this.nts_exprListNTS = nts_exprListNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> temp = new ArrayList<>();
        temp.add(nts_expr.toAbsSyntax());
        return nts_exprListNTS.toAbsSyntax(temp);
    }
}
