package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IDecl;

import java.util.ArrayList;

// cpsDecl ::= decl cpsDeclNTS
public class CpsDecl extends Production implements ICpsDecl {
    private IDecl nts_decl;
    private ICpsDeclNTS nts_cpsDeclNTS;

    public CpsDecl(IDecl nts_decl, ICpsDeclNTS nts_cpsDeclNTS) {
        this.nts_decl = nts_decl;
        this.nts_cpsDeclNTS = nts_cpsDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> temp = new ArrayList<>();
        temp.add(nts_decl.toAbsSyntax());

        return nts_cpsDeclNTS.toAbsSyntax(temp);
    }
}
