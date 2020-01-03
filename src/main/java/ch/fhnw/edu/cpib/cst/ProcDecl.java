package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
public class ProcDecl extends Production implements IProcDecl {
    private IToken ts_proc;
    private IToken ts_ident;
    private IParamList nts_paramList;
    private IProcDeclNTS nts_procDeclNTS;
    private IToken ts_do;
    private ICpsCmd nts_cpsCmd;
    private IToken ts_endProc;

    public ProcDecl(IToken ts_proc, IToken ts_ident, IParamList nts_paramList,
            IProcDeclNTS nts_procDeclNTS, IToken ts_do, ICpsCmd nts_cpsCmd, IToken ts_endProc) {
        this.ts_proc = ts_proc;
        this.ts_ident = ts_ident;
        this.ts_do = ts_do;
        this.ts_endProc = ts_endProc;
        this.nts_paramList = nts_paramList;
        this.nts_procDeclNTS = nts_procDeclNTS;
        this.nts_cpsCmd = nts_cpsCmd;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.ProcDecl((Ident) ts_ident, nts_paramList.toAbsSyntax(),
                nts_procDeclNTS.toAbsSyntax(), nts_cpsCmd.toAbsSyntax());
    }
}
