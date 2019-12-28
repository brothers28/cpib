package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// monadicOpr ::= NOT
public class MonadicOprNot extends Production implements IMonadicOpr {
    protected final IToken T_not;

    public MonadicOprNot(final IToken t_not) {
        T_not = t_not;
    }

    @Override public Operator toAbsSyntax() {
        return (Operator) T_not;
    }
}
