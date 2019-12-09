package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Literal;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// factor ::= LITERAL
public class FactorLiteral extends Production implements IFactor {
    protected final Token T_literal;

    public FactorLiteral(final Token t_literal) {
        T_literal = t_literal;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.LiteralFactor((Literal)T_literal);
    }
}
