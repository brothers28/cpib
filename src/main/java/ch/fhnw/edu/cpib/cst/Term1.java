package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ITerm1;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2;

// term1 ::= term2 term1NTS
public class Term1 extends Production implements ITerm1 {
    protected final ITerm2 nts_term2;
    protected final ITerm1NTS nts_term1NTS;

    public Term1(final ITerm2 nts_term2, final ITerm1NTS nts_term1NTS) {
        this.nts_term2 = nts_term2;
        this.nts_term1NTS = nts_term1NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return nts_term1NTS.toAbsSyntax(nts_term2.toAbsSyntax());
    }
}
