package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= IF expr THEN cpsCmd ifElseNTS ENDIF
public class CmdIf extends Production implements ICmd {
    private IToken ts_if;
    private IExpr nts_expr;
    private IToken ts_then;
    private ICpsCmd nts_cpsCmd;
    private IIfElseNTS nts_ifElseNts;
    private IToken ts_endIf;

    public CmdIf(IToken ts_if, IExpr nts_expr, IToken ts_then, ICpsCmd nts_cpsCmd,
            IIfElseNTS nts_ifElseNts, IToken ts_endIf) {
        this.ts_if = ts_if;
        this.nts_expr = nts_expr;
        this.ts_then = ts_then;
        this.nts_cpsCmd = nts_cpsCmd;
        this.nts_ifElseNts = nts_ifElseNts;
        this.ts_endIf = ts_endIf;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.IfCmd(nts_expr.toAbsSyntax(), nts_cpsCmd.toAbsSyntax(), nts_ifElseNts.toAbsSyntax());
    }
}
