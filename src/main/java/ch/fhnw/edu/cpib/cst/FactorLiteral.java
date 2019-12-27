package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Literal;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

/**
 * @Author Hussein Farzi
 */

// factor ::= LITERAL
public class FactorLiteral extends Production implements IFactor {
    protected final IToken T_literal;

    public FactorLiteral(final IToken t_literal) {
        T_literal = t_literal;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.LiteralFactor((Literal)T_literal);
    }
}
