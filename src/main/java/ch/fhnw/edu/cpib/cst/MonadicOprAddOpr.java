package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// monadicOpr ::= ADDOPR
public class MonadicOprAddOpr extends Production implements IMonadicOpr {
    protected final IToken T_addOpr;

    public MonadicOprAddOpr(final IToken t_addOpr) {
        T_addOpr = t_addOpr;
    }

    @Override public Operator toAbsSyntax() {
        return (Operator) T_addOpr;
    }
}
