package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// monadicOpr ::= ADDOPR
public class MonadicOprAddOpr extends Production implements IMonadicOpr {
    protected final Token T_addOpr;

    public MonadicOprAddOpr(final Token t_addOpr) {
        T_addOpr = t_addOpr;
    }

    @Override
    public Operator toAbsSyn() {
        return (Operator) T_addOpr;
    }
}
