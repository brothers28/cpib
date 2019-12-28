package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm1NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term1NTS ::= RELOPR term2 term1NTS
public class Term1NTS extends Production implements ITerm1NTS {
    protected final IToken T_relOpr;
    protected final ITerm2 N_term2;
    protected final ITerm1NTS N_term1NTS;

    public Term1NTS(final IToken t_relOpr, final ITerm2 n_term2, final ITerm1NTS n_term1NTS) {
        T_relOpr = t_relOpr;
        N_term2 = n_term2;
        N_term1NTS = n_term1NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.RelExpr(
                ((Operator) T_relOpr).getOperator(), expr, N_term2.toAbsSyntax());
        return N_term1NTS.toAbsSyntax(temp);
    }
}
