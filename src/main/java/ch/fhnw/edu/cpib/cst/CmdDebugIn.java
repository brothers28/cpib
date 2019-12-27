package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= DEBUGIN expr
public class CmdDebugIn extends Production implements ICmd {
    protected final IToken T_debugIn;
    protected final IExpr N_expr;

    public CmdDebugIn(final IToken T_debugIn, final IExpr N_expr) {
        this.T_debugIn = T_debugIn;
        this.N_expr = N_expr;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.DebugInCmd(N_expr.toAbsSyn());
    }
}
