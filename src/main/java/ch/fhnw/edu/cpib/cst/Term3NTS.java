package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3NTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term3NTS ::= MULTOPR factor term3NTS
public class Term3NTS extends Production implements ITerm3NTS {
    protected final IToken ts_multOpr;
    protected final IFactor nts_factor;
    protected final ITerm3NTS nts_term3NTS;

    public Term3NTS(final IToken ts_multOpr, final IFactor nts_factor, final ITerm3NTS nts_term3NTS) {
        this.ts_multOpr = ts_multOpr;
        this.nts_factor = nts_factor;
        this.nts_term3NTS = nts_term3NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.MultExpr(
                ((Operator) ts_multOpr).getOperator(), expr, nts_factor.toAbsSyntax());
        return nts_term3NTS.toAbsSyntax(temp);
    }
}
