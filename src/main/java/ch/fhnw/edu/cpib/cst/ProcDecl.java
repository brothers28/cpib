package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
public class ProcDecl extends Production implements IProcDecl {
    protected final IToken T_proc;
    protected final IToken T_ident;
    protected final IParamList N_paramList;
    protected final IProcDeclNTS N_procDeclNTS;
    protected final IToken T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final IToken T_endProc;

    public ProcDecl(final IToken t_proc, final IToken t_ident, final IParamList n_paramList,
            final IProcDeclNTS n_procDeclNTS, final IToken t_do, final ICpsCmd n_cpsCmd, final IToken t_endProc) {
        T_proc = t_proc;
        T_ident = t_ident;
        T_do = t_do;
        T_endProc = t_endProc;
        N_paramList = n_paramList;
        N_procDeclNTS = n_procDeclNTS;
        N_cpsCmd = n_cpsCmd;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.ProcDecl((Ident) T_ident, N_paramList.toAbsSyn(), N_procDeclNTS.toAbsSyn(),
                N_cpsCmd.toAbsSyn());
    }
}
