package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ITerm2;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;

// term2 ::= term3 term2NTS
public class Term2 extends Production implements ITerm2 {
    protected ITerm3 nts_term3;
    protected ITerm2NTS nts_term2NTS;

    public Term2(ITerm3 nts_term3, ITerm2NTS nts_term2NTS) {
        this.nts_term3 = nts_term3;
        this.nts_term2NTS = nts_term2NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return nts_term2NTS.toAbsSyntax(nts_term3.toAbsSyntax());
    }
}
