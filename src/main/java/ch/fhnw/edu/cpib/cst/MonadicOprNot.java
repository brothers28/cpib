package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// monadicOpr ::= NOT
public class MonadicOprNot extends Production implements IMonadicOpr {
    protected final IToken ts_not;

    public MonadicOprNot(final IToken ts_not) {
        this.ts_not = ts_not;
    }

    @Override public Operator toAbsSyntax() {
        return (Operator) ts_not;
    }
}
