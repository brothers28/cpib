package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.*;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
public class FunDecl extends Production implements IFunDecl {
    protected IToken ts_fun;
    protected IToken ts_ident;
    protected IParamList nts_paramList;
    protected IToken ts_returns;
    protected IStoDecl nts_stoDecl;
    protected IFunDeclNTS nts_funDeclNTS;
    protected IToken ts_do;
    protected ICpsCmd nts_cpsCmd;
    protected IToken ts_endFun;

    public FunDecl(IToken ts_fun, IToken ts_ident, IParamList nts_paramList, IToken ts_returns, IStoDecl nts_stoDecl,
            IFunDeclNTS nts_funDeclNTS, IToken ts_do, ICpsCmd nts_cpsCmd, IToken ts_endFun) {
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
        return new ch.fhnw.edu.cpib.ast.FunDecl((Ident) ts_ident, nts_paramList.toAbsSyntax(),
                nts_stoDecl.toAbsSyntax(), nts_funDeclNTS.toAbsSyntax(), nts_cpsCmd.toAbsSyntax());
    }
}
