package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= WHILE expr DO cpsCmd ENDWHILE
public class CmdWhileDo extends Production implements ICmd {
    protected final Token T_while;
    protected final IExpr N_expr;
    protected final Token T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final Token T_endWhile;

    public CmdWhileDo(final Token T_while,
                      final IExpr N_expr,
                      final Token T_do,
                      final ICpsCmd N_cpsCmd,
                      final Token T_endWhile) {
        this.T_while = T_while;
        this.N_expr = N_expr;
        this.T_do = T_do;
        this.N_cpsCmd = N_cpsCmd;
        this.T_endWhile = T_endWhile;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.WhileCmd(N_expr.toAbsSyn(), N_cpsCmd.toAbsSyn());
    }
}