package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= WHILE expr DO cpsCmd ENDWHILE
public class CmdWhile extends Production implements ICmd {
    protected final IToken T_while;
    protected final IExpr N_expr;
    protected final IToken T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final IToken T_endWhile;

    public CmdWhile(final IToken T_while, final IExpr N_expr, final IToken T_do, final ICpsCmd N_cpsCmd,
            final IToken T_endWhile) {
        this.T_while = T_while;
        this.N_expr = N_expr;
        this.T_do = T_do;
        this.N_cpsCmd = N_cpsCmd;
        this.T_endWhile = T_endWhile;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.WhileCmd(N_expr.toAbsSyntax(), N_cpsCmd.toAbsSyntax());
    }
}
