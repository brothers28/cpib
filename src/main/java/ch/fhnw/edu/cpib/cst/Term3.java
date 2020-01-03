package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3NTS;

// term3 ::= factor term3NTS
public class Term3 extends Production implements ITerm3 {
    protected IFactor nts_factor;
    protected ITerm3NTS nts_term3NTS;

    public Term3(IFactor nts_factor, ITerm3NTS nts_term3NTS) {
        this.nts_factor = nts_factor;
        this.nts_term3NTS = nts_term3NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return nts_term3NTS.toAbsSyntax(nts_factor.toAbsSyntax());
    }
}
