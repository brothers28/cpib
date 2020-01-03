package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICastOpr;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;

// factor ::= castOpr factor
public class FactorCastOpr extends Production implements IFactor {
    protected final ICastOpr nts_castOpr;
    protected final IFactor nts_mfactor;

    public FactorCastOpr(final ICastOpr nts_castOpr, final IFactor nts_mfactor) {
        this.nts_castOpr = nts_castOpr;
        this.nts_mfactor = nts_mfactor;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.CastFactor(nts_castOpr.toAbsSyntax(), nts_mfactor.toAbsSyntax());
    }
}
