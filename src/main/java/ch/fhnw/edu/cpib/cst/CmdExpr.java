package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= expr BECOMES expr
public class CmdExpr extends Production implements ICmd {
    private IExpr nts_expr1;
    private IToken ts_becomes;
    private IExpr nts_expr2;

    public CmdExpr(IExpr nts_expr1, IToken ts_becomes, IExpr nts_expr2) {
        this.nts_expr1 = nts_expr1;
        this.ts_becomes = ts_becomes;
        this.nts_expr2 = nts_expr2;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.AssignCmd(nts_expr1.toAbsSyntax(), nts_expr2.toAbsSyntax());
    }
}
