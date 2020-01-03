package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// factor ::= IDENT factorNTS
public class FactorIdent extends Production implements IFactor {
    protected IToken ts_ident;
    protected IFactorNTS nts_factorNTS;

    public FactorIdent(IToken ts_ident, IFactorNTS nts_factorNTS) {
        this.ts_ident = ts_ident;
        this.nts_factorNTS = nts_factorNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return nts_factorNTS.toAbsSyntax((Ident) ts_ident);
    }
}
