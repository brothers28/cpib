package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= DEBUGIN expr
public class CmdDebugIn extends Production implements ICmd {
    private IToken ts_debugIn;
    private IExpr nts_expr;

    public CmdDebugIn(IToken ts_debugIn, IExpr nts_expr) {
        this.ts_debugIn = ts_debugIn;
        this.nts_expr = nts_expr;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.DebugInCmd(nts_expr.toAbsSyntax());
    }
}
