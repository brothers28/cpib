package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= expr BECOMES expr
public class CmdExpr extends Production implements ICmd {
    protected final IExpr N_expr1;
    protected final Token T_becomes;
    protected final IExpr N_expr2;

    public CmdExpr(final IExpr N_expr1,
                   final Token T_becomes,
                   final IExpr N_expr2) {
        this.N_expr1 = N_expr1;
        this.T_becomes = T_becomes;
        this.N_expr2 = N_expr2;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.AssignCmd(N_expr1.toAbsSyn(), N_expr2.toAbsSyn());
    }
}
