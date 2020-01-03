package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.IMonadicOpr;

// factor ::= monadicOpr factor
public class FactorMonadicOpr extends Production implements IFactor {
    private IMonadicOpr nts_monadicOpr;
    private IFactor nts_mfactor;

    public FactorMonadicOpr(IMonadicOpr nts_monadicOpr, IFactor nts_mfactor) {
        this.nts_monadicOpr = nts_monadicOpr;
        this.nts_mfactor = nts_mfactor;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.MonadicFactor(nts_monadicOpr.toAbsSyntax(), nts_mfactor.toAbsSyntax());
    }
}
