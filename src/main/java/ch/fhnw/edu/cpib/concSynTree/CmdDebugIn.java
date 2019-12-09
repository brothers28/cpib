package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= DEBUGIN expr
public class CmdDebugIn extends Production implements ICmd {
    protected final Token T_debugIn;
    protected final IExpr N_expr;

    public CmdDebugIn(final Token T_debugIn,
                      final IExpr N_expr) {
        this.T_debugIn = T_debugIn;
        this.N_expr = N_expr;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.DebugInCmd(N_expr.toAbsSyn());
    }
}