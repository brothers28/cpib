package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= DEBUGOUT expr
public class CmdDebugOut extends Production implements ICmd {
    protected IToken ts_debugOut;
    protected IExpr nts_expr;

    public CmdDebugOut(IToken ts_debugOut, IExpr nts_expr) {
        this.ts_debugOut = ts_debugOut;
        this.nts_expr = nts_expr;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.DebugOutCmd(nts_expr.toAbsSyntax());
    }
}
