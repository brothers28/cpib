package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// procDeclNTS ::= LOCAL cpsStoDecl
public class ProcDeclNTS extends Production implements IProcDeclNTS {
    protected final IToken ts_local;
    protected final ICpsStoDecl nts_cpsStoDecl;

    public ProcDeclNTS(final IToken ts_local, final ICpsStoDecl nts_cpsStoDecl) {
        this.ts_local = ts_local;
        this.nts_cpsStoDecl = nts_cpsStoDecl;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax() {
        return nts_cpsStoDecl.toAbsSyntax();
    }
}
