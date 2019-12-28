package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ITerm2;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;

// term2 ::= term3 term2NTS
public class Term2 extends Production implements ITerm2 {
    protected final ITerm3 N_term3;
    protected final ITerm2NTS N_term2NTS;

    public Term2(final ITerm3 n_term3, final ITerm2NTS n_term2NTS) {
        N_term3 = n_term3;
        N_term2NTS = n_term2NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return N_term2NTS.toAbsSyntax(N_term3.toAbsSyntax());
    }
}
