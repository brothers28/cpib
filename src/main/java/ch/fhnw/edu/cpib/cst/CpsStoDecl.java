package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;

import java.util.ArrayList;

// cpsStoDecl ::= stoDecl cpsStoDeclNTS
public class CpsStoDecl extends Production implements ICpsStoDecl {
    protected IStoDecl nts_stoDecl;
    protected ICpsStoDeclNTS nts_cpsStoDeclNTS;

    public CpsStoDecl(IStoDecl nts_stoDecl, ICpsStoDeclNTS nts_cpsStoDeclNTS) {
        this.nts_stoDecl = nts_stoDecl;
        this.nts_cpsStoDeclNTS = nts_cpsStoDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp = new ArrayList<>();
        temp.add(nts_stoDecl.toAbsSyntax());
        return nts_cpsStoDeclNTS.toAbsSyntax(temp);
    }
}
