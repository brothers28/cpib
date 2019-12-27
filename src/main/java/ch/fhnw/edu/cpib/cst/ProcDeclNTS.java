package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// procDeclNTS ::= LOCAL cpsStoDecl
public class ProcDeclNTS extends Production implements IProcDeclNTS {
    protected final IToken T_local;
    protected final ICpsStoDecl N_cpsStoDecl;

    public ProcDeclNTS(final IToken t_local, final ICpsStoDecl n_cpsStoDecl) {
        T_local = t_local;
        N_cpsStoDecl = n_cpsStoDecl;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyn() {
        return N_cpsStoDecl.toAbsSyn();
    }
}
