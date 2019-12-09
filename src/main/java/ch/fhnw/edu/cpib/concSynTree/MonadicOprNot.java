package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// monadicOpr ::= NOT
public class MonadicOprNot extends Production implements IMonadicOpr {
    protected final Token T_not;

    public MonadicOprNot(final Token t_not) {
        T_not = t_not;
    }

    @Override
    public Operator toAbsSyn() {
        return (Operator) T_not;
    }
}
