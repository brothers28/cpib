package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Literal;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factor ::= LITERAL
public class FactorLiteral extends Production implements IFactor {
    protected final IToken ts_literal;

    public FactorLiteral(final IToken ts_literal) {
        this.ts_literal = ts_literal;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.LiteralFactor((Literal) ts_literal);
    }
}
