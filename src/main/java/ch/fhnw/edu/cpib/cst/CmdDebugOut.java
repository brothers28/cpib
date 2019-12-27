package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= DEBUGOUT expr
public class CmdDebugOut extends Production implements ICmd {
    protected final IToken T_debugOut;
    protected final IExpr N_expr;

    public CmdDebugOut(final IToken T_debugOut, final IExpr N_expr) {
        this.T_debugOut = T_debugOut;
        this.N_expr = N_expr;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.DebugOutCmd(N_expr.toAbsSyn());
    }
}
