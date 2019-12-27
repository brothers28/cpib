package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= CALL IDENT exprList
public class CmdCallIdentExprList extends Production implements ICmd {
    protected final IToken T_call;
    protected final IToken T_ident;
    protected final IExprList N_exprList;

    public CmdCallIdentExprList(final IToken T_call,
                                final IToken T_ident,
                                final IExprList N_exprList) {
        this.T_call = T_call;
        this.T_ident = T_ident;
        this.N_exprList = N_exprList;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.ProcCallCmd((Ident)T_ident, N_exprList.toAbsSyn());
    }
}
