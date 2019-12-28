package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factor ::= IDENT factorNTS
public class FactorIdent extends Production implements IFactor {
    protected final IToken T_ident;
    protected final IFactorNTS N_factorNTS;

    public FactorIdent(final IToken t_ident, final IFactorNTS n_factorNTS) {
        T_ident = t_ident;
        N_factorNTS = n_factorNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return N_factorNTS.toAbsSyntax((Ident) T_ident);
    }
}
