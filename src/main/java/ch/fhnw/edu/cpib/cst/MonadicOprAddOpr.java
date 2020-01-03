package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// monadicOpr ::= ADDOPR
public class MonadicOprAddOpr extends Production implements IMonadicOpr {
    private IToken ts_addOpr;

    public MonadicOprAddOpr(IToken ts_addOpr) {
        this.ts_addOpr = ts_addOpr;
    }

    @Override public Operator toAbsSyntax() {
        return (Operator) ts_addOpr;
    }
}
