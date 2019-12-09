package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
public class CmdIfThen extends Production implements ICmd {
    protected final Token T_if;
    protected final IExpr N_expr;
    protected final Token T_then;
    protected final ICpsCmd N_cpsCmd;
    protected final IIfElseNTS N_ifelseNTS;
    protected final Token T_endIf;

    public CmdIfThen(final Token T_if,
                     final IExpr N_expr,
                     final Token T_then,
                     final ICpsCmd N_cpsCmd,
                     final IIfElseNTS N_ifelseNTS,
                     final Token T_endIf) {
        this.T_if = T_if;
        this.N_expr = N_expr;
        this.T_then = T_then;
        this.N_cpsCmd = N_cpsCmd;
        this.N_ifelseNTS = N_ifelseNTS;
        this.T_endIf = T_endIf;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.IfCmd(N_expr.toAbsSyn(), N_cpsCmd.toAbsSyn(), N_ifelseNTS.toAbsSyn());
    }
}