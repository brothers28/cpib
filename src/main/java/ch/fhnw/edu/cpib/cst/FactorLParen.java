package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

/**
 * @Author Hussein Farzi
 */

// factor ::= LPAREN expr RPAREN
public class FactorLParen extends Production implements IFactor {
    protected final IToken T_lParen;
    protected final IExpr N_expr;
    protected final IToken T_rParen;

    public FactorLParen(final IToken t_lParen,
                        final IExpr n_expr,
                        final IToken t_rParen) {
        T_lParen = t_lParen;
        T_rParen = t_rParen;
        N_expr = n_expr;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.ExprFactor(N_expr.toAbsSyn());
    }
}
