package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factorNTS ::= INIT
public class FactorNTSInit extends Production implements IFactorNTS {
    private IToken ts_init;

    public FactorNTSInit(IToken ts_init) {
        this.ts_init = ts_init;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax(Ident ident) {
        return new ch.fhnw.edu.cpib.ast.InitFactor(ident, true);
    }
}
