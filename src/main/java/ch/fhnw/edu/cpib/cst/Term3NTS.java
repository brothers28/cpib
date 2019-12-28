package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IFactor;
import ch.fhnw.edu.cpib.cst.interfaces.ITerm3NTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

// term3NTS ::= MULTOPR factor term3NTS
public class Term3NTS extends Production implements ITerm3NTS {
    protected final IToken T_multOpr;
    protected final IFactor N_factor;
    protected final ITerm3NTS N_term3NTS;

    public Term3NTS(final IToken t_multOpr, final IFactor n_factor, final ITerm3NTS n_term3NTS) {
        T_multOpr = t_multOpr;
        N_factor = n_factor;
        N_term3NTS = n_term3NTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(IExpr expr) {
        ch.fhnw.edu.cpib.ast.interfaces.IExpr temp = new ch.fhnw.edu.cpib.ast.MultExpr(
                ((Operator) T_multOpr).getOperator(), expr, N_factor.toAbsSyntax());
        return N_term3NTS.toAbsSyntax(temp);
    }
}
