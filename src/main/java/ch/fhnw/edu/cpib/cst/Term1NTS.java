package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term1NTS ::= RELOPR term2 term1NTS
public class Term1NTS extends Production implements ITerm1NTS {
    protected IToken ts_relOpr;
    protected ITerm2 nts_term2;
    protected ITerm1NTS nts_term1NTS;

    public Term1NTS(IToken ts_relOpr, ITerm2 nts_term2, ITerm1NTS nts_term1NTS) {
        this.ts_relOpr = ts_relOpr;
        this.nts_term2 = nts_term2;
        this.nts_term1NTS = nts_term1NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.RelExpr(
                ((Operator) ts_relOpr).getOperator(), expr, nts_term2.toAbsSyntax());
        return nts_term1NTS.toAbsSyntax(temp);
    }
}
