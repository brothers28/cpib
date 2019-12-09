package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamList;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IProcDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

/**
 * @Author Hussein Farzi
 */

// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
public class ProcDecl extends Production implements IProcDecl {
    protected final Token T_proc;
    protected final Token T_ident;
    protected final IParamList N_paramList;
    protected final IProcDeclNTS N_procDeclNTS;
    protected final Token T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final Token T_endProc;

    public ProcDecl(final Token t_proc,
                    final Token t_ident,
                    final IParamList n_paramList,
                    final IProcDeclNTS n_procDeclNTS,
                    final Token t_do,
                    final ICpsCmd n_cpsCmd,
                    final Token t_endProc) {
        T_proc = t_proc;
        T_ident = t_ident;
        T_do = t_do;
        T_endProc = t_endProc;
        N_paramList = n_paramList;
        N_procDeclNTS = n_procDeclNTS;
        N_cpsCmd = n_cpsCmd;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.ProcDecl((Ident)T_ident, N_paramList.toAbsSyn(), N_procDeclNTS.toAbsSyn(), N_cpsCmd.toAbsSyn());
    }
}
