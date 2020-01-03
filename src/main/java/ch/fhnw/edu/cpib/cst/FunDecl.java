package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
public class FunDecl extends Production implements IFunDecl {
    protected final IToken ts_fun;
    protected final IToken ts_ident;
    protected final IParamList nts_paramList;
    protected final IToken ts_returns;
    protected final IStoDecl nts_stoDecl;
    protected final IFunDeclNTS nts_funDeclNTS;
    protected final IToken ts_do;
    protected final ICpsCmd nts_cpsCmd;
    protected final IToken ts_endFun;

    public FunDecl(final IToken ts_fun, final IToken ts_ident, final IParamList nts_paramList, final IToken ts_returns,
            final IStoDecl nts_stoDecl, final IFunDeclNTS nts_funDeclNTS, final IToken ts_do, final ICpsCmd nts_cpsCmd,
            final IToken ts_endFun) {
        this.ts_fun = ts_fun;
        this.ts_ident = ts_ident;
        this.nts_paramList = nts_paramList;
        this.ts_returns = ts_returns;
        this.nts_stoDecl = nts_stoDecl;
        this.nts_funDeclNTS = nts_funDeclNTS;
        this.ts_do = ts_do;
        this.nts_cpsCmd = nts_cpsCmd;
        this.ts_endFun = ts_endFun;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.FunDecl((Ident) ts_ident, nts_paramList.toAbsSyntax(), nts_stoDecl.toAbsSyntax(),
                nts_funDeclNTS.toAbsSyntax(), nts_cpsCmd.toAbsSyntax());
    }
}
