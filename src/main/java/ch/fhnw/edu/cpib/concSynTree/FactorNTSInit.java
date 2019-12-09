package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// factorNTS ::= INIT
public class FactorNTSInit extends Production implements IFactorNTS {
    protected final Token T_init;

    public FactorNTSInit(final Token t_init) {
        T_init = t_init;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn(Ident ident) {
        return new ch.fhnw.edu.cpib.absSynTree.InitFactor(ident, true);
    }
}
