package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

// funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
public class FunDecl extends Production implements IFunDecl {
    protected final Token T_fun;
    protected final Token T_ident;
    protected final IParamList N_paramList;
    protected final Token T_returns;
    protected final IStoDecl N_stoDecl;
    protected final IFunDeclNTS N_funDeclNTS;
    protected final Token T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final Token T_endFun;

    public FunDecl(final Token T_fun,
                   final Token T_ident,
                   final IParamList N_paramList,
                   final Token T_returns,
                   final IStoDecl N_stoDecl,
                   final IFunDeclNTS N_funDeclNTS,
                   final Token T_do,
                   final ICpsCmd N_cpsCmd,
                   final Token T_endFun) {
        this.T_fun = T_fun;
        this.T_ident = T_ident;
        this.N_paramList = N_paramList;
        this.T_returns = T_returns;
        this.N_stoDecl = N_stoDecl;
        this.N_funDeclNTS = N_funDeclNTS;
        this.T_do = T_do;
        this.N_cpsCmd = N_cpsCmd;
        this.T_endFun = T_endFun;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.FunDecl((Ident) T_ident,
                N_paramList.toAbsSyn(), N_stoDecl.toAbsSyn(),
                N_funDeclNTS.toAbsSyn(), N_cpsCmd.toAbsSyn());
    }
}
