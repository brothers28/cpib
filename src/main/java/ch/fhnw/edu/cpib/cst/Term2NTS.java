package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term2NTS ::= ADDOPR term3 term2NTS
public class Term2NTS extends Production implements ITerm2NTS {
    private IToken ts_addOpr;
    private ITerm3 nts_term3;
    private ITerm2NTS nts_term2NTS;

    public Term2NTS(IToken ts_addOpr, ITerm3 nts_term3, ITerm2NTS nts_term2NTS) {
        this.ts_addOpr = ts_addOpr;
        this.nts_term3 = nts_term3;
        this.nts_term2NTS = nts_term2NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.AddExpr(
                ((Operator) ts_addOpr).getOperator(), expr, nts_term3.toAbsSyntax());
        return nts_term2NTS.toAbsSyntax(temp);
    }
}
