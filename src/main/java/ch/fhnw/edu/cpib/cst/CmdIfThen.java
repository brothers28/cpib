package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
public class CmdIfThen extends Production implements ICmd {
    protected final IToken T_if;
    protected final IExpr N_expr;
    protected final IToken T_then;
    protected final ICpsCmd N_cpsCmd;
    protected final IIfElseNTS N_ifelseNTS;
    protected final IToken T_endIf;

    public CmdIfThen(final IToken T_if, final IExpr N_expr, final IToken T_then, final ICpsCmd N_cpsCmd,
            final IIfElseNTS N_ifelseNTS, final IToken T_endIf) {
        this.T_if = T_if;
        this.N_expr = N_expr;
        this.T_then = T_then;
        this.N_cpsCmd = N_cpsCmd;
        this.N_ifelseNTS = N_ifelseNTS;
        this.T_endIf = T_endIf;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.IfCmd(N_expr.toAbsSyn(), N_cpsCmd.toAbsSyn(), N_ifelseNTS.toAbsSyn());
    }
}
