package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprList;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= CALL IDENT exprList
public class CmdCallIdentExprList extends Production implements ICmd {
    protected final Token T_call;
    protected final Token T_ident;
    protected final IExprList N_exprList;

    public CmdCallIdentExprList(final Token T_call,
                                final Token T_ident,
                                final IExprList N_exprList) {
        this.T_call = T_call;
        this.T_ident = T_ident;
        this.N_exprList = N_exprList;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.ProcCallCmd((Ident)T_ident, N_exprList.toAbsSyn());
    }
}