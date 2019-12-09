package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= DEBUGOUT expr
public class CmdDebugOut extends Production implements ICmd {
    protected final Token T_debugOut;
    protected final IExpr N_expr;

    public CmdDebugOut(final Token T_debugOut,
                       final IExpr N_expr) {
        this.T_debugOut = T_debugOut;
        this.N_expr = N_expr;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.DebugOutCmd(N_expr.toAbsSyn());
    }
}