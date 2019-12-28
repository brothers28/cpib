package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= expr BECOMES expr
public class CmdExpr extends Production implements ICmd {
    protected final IExpr N_expr1;
    protected final IToken T_becomes;
    protected final IExpr N_expr2;

    public CmdExpr(final IExpr N_expr1, final IToken T_becomes, final IExpr N_expr2) {
        this.N_expr1 = N_expr1;
        this.T_becomes = T_becomes;
        this.N_expr2 = N_expr2;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.AssignCmd(N_expr1.toAbsSyntax(), N_expr2.toAbsSyntax());
    }
}
