package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// factor ::= IDENT factorNTS
public class FactorIdent extends Production implements IFactor {
    protected final Token T_ident;
    protected final IFactorNTS N_factorNTS;

    public FactorIdent(final Token t_ident,
                       final IFactorNTS n_factorNTS) {
        T_ident = t_ident;
        N_factorNTS = n_factorNTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn() {
        return N_factorNTS.toAbsSyn((Ident) T_ident);
    }
}
