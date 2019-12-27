package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;

import java.util.ArrayList;

// cpsStoDecl ::= stoDecl cpsStoDeclNTS
public class CpsStoDecl extends Production implements ICpsStoDecl {
    protected final IStoDecl N_stoDecl;
    protected final ICpsStoDeclNTS N_cpsStoDeclNTS;

    public CpsStoDecl(final IStoDecl n_stoDecl, final ICpsStoDeclNTS n_cpsStoDeclNTS) {
        N_stoDecl = n_stoDecl;
        N_cpsStoDeclNTS = n_cpsStoDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyn() {
        ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp = new ArrayList<>();
        temp.add(N_stoDecl.toAbsSyn());
        return N_cpsStoDeclNTS.toAbsSyn(temp);
    }
}
