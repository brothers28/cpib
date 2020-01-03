package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
public class ProcDecl extends Production implements IProcDecl {
    protected final IToken ts_proc;
    protected final IToken ts_ident;
    protected final IParamList nts_paramList;
    protected final IProcDeclNTS nts_procDeclNTS;
    protected final IToken ts_do;
    protected final ICpsCmd nts_cpsCmd;
    protected final IToken ts_endProc;

    public ProcDecl(final IToken ts_proc, final IToken ts_ident, final IParamList nts_paramList,
            final IProcDeclNTS nts_procDeclNTS, final IToken ts_do, final ICpsCmd nts_cpsCmd, final IToken ts_endProc) {
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
