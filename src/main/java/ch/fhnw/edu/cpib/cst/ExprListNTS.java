package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// exprListNTS ::= COMMA expr exprListNTS
public class ExprListNTS extends Production implements IExprListNTS {
    private IToken ts_comma;
    private IExpr nts_expr;
    private IExprListNTS nts_exprListNTS;

    public ExprListNTS(IToken ts_comma, IExpr nts_expr, IExprListNTS nts_exprListNTS) {
        this.ts_comma = ts_comma;
        this.nts_expr = nts_expr;
        this.nts_exprListNTS = nts_exprListNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> toAbsSyntax(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> temp) {
        temp.add(nts_expr.toAbsSyntax());
        return nts_exprListNTS.toAbsSyntax(temp);
    }
}
