package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprNTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// exprNTS ::= BOOLOPR term1 exprNTS
public class ExprNTS extends Production implements IExprNTS {
    protected final IToken ts_boolOpr;
    protected final ITerm1 nts_term1;
    protected final IExprNTS nts_exprNTS;

    public ExprNTS(final IToken ts_boolOpr, final ITerm1 nts_term1, final IExprNTS nts_exprNTS) {
        this.ts_boolOpr = ts_boolOpr;
        this.nts_term1 = nts_term1;
        this.nts_exprNTS = nts_exprNTS;
    }

    @Override public IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.BoolExpr(
                ((Operator) ts_boolOpr).getOperator(), expr, nts_term1.toAbsSyntax());
        return nts_exprNTS.toAbsSyntax(temp);
    }
}
