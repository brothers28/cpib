package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3NTS;

// term3 ::= factor term3NTS
public class Term3 extends Production implements ITerm3 {
    protected final IFactor N_factor;
    protected final ITerm3NTS N_term3NTS;

    public Term3(final IFactor n_factor, final ITerm3NTS n_term3NTS) {
        N_factor = n_factor;
        N_term3NTS = n_term3NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax() {
        return N_term3NTS.toAbsSyntax(N_factor.toAbsSyntax());
    }
}
