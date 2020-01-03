package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
public class CmdIf extends Production implements ICmd {
    protected final IToken ts_if;
    protected final IExpr nts_expr;
    protected final IToken ts_then;
    protected final ICpsCmd nts_cpsCmd;
    protected final IIfElseNTS nts_ifelseNTS;
    protected final IToken ts_endIf;

    public CmdIf(final IToken ts_if, final IExpr nts_expr, final IToken ts_then, final ICpsCmd nts_cpsCmd,
            final IIfElseNTS nts_ifelseNTS, final IToken ts_endIf) {
        this.ts_if = ts_if;
        this.nts_expr = nts_expr;
        this.ts_then = ts_then;
        this.nts_cpsCmd = nts_cpsCmd;
        this.nts_ifelseNTS = nts_ifelseNTS;
        this.ts_endIf = ts_endIf;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.IfCmd(nts_expr.toAbsSyntax(), nts_cpsCmd.toAbsSyntax(), nts_ifelseNTS.toAbsSyntax());
    }
}
