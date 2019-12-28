package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm2NTS;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term2NTS ::= ADDOPR term3 term2NTS
public class Term2NTS extends Production implements ITerm2NTS {
    protected final IToken T_addOpr;
    protected final ITerm3 N_term3;
    protected final ITerm2NTS N_term2NTS;

    public Term2NTS(final IToken t_addOpr, final ITerm3 n_term3, final ITerm2NTS n_term2NTS) {
        T_addOpr = t_addOpr;
        N_term3 = n_term3;
        N_term2NTS = n_term2NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.AddExpr(
                ((Operator) T_addOpr).getOperator(), expr, N_term3.toAbsSyntax());
        return N_term2NTS.toAbsSyntax(temp);
    }
}
