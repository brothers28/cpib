package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IDecl;

import java.util.ArrayList;

// cpsDecl ::= decl cpsDeclNTS
public class CpsDecl extends Production implements ICpsDecl {
    protected final IDecl N_decl;
    protected final ICpsDeclNTS N_cpsDeclNTS;

    public CpsDecl(final IDecl N_decl, final ICpsDeclNTS N_cpsDeclNTS) {
        this.N_decl = N_decl;
        this.N_cpsDeclNTS = N_cpsDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> temp = new ArrayList<>();
        temp.add(N_decl.toAbsSyntax());

        return N_cpsDeclNTS.toAbsSyntax(temp);
    }
}
