package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= WHILE expr DO cpsCmd ENDWHILE
public class CmdWhile extends Production implements ICmd {
    protected final IToken ts_while;
    protected final IExpr nts_expr;
    protected final IToken ts_do;
    protected final ICpsCmd nts_cpsCmd;
    protected final IToken ts_endWhile;

    public CmdWhile(final IToken ts_while, final IExpr nts_expr, final IToken ts_do, final ICpsCmd nts_cpsCmd,
            final IToken ts_endWhile) {
        this.ts_while = ts_while;
        this.nts_expr = nts_expr;
        this.ts_do = ts_do;
        this.nts_cpsCmd = nts_cpsCmd;
        this.ts_endWhile = ts_endWhile;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.WhileCmd(nts_expr.toAbsSyntax(), nts_cpsCmd.toAbsSyntax());
    }
}
