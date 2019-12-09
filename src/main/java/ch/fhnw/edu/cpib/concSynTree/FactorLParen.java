package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// factor ::= LPAREN expr RPAREN
public class FactorLParen extends Production implements IFactor {
    protected final Token T_lParen;
    protected final IExpr N_expr;
    protected final Token T_rParen;

    public FactorLParen(final Token t_lParen,
                        final IExpr n_expr,
                        final Token t_rParen) {
        T_lParen = t_lParen;
        T_rParen = t_rParen;
        N_expr = n_expr;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.ExprFactor(N_expr.toAbsSyn());
    }
}
