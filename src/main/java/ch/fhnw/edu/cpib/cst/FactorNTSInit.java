package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factorNTS ::= INIT
public class FactorNTSInit extends Production implements IFactorNTS {
    protected final IToken T_init;

    public FactorNTSInit(final IToken t_init) {
        T_init = t_init;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyn(Ident ident) {
        return new ch.fhnw.edu.cpib.ast.InitFactor(ident, true);
    }
}
