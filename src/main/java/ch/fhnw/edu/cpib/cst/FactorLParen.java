package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factor ::= LPAREN expr RPAREN
public class FactorLParen extends Production implements IFactor {
    protected final IToken ts_lParen;
    protected final IExpr nts_expr;
    protected final IToken ts_rParen;

    public FactorLParen(final IToken ts_lParen, final IExpr nts_expr, final IToken ts_rParen) {
        this.ts_lParen = ts_lParen;
        this.ts_rParen = ts_rParen;
        this.nts_expr = nts_expr;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.ExprFactor(nts_expr.toAbsSyntax());
    }
}
