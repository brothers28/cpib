package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// globalNTS ::= GLOBAL cpsDecl
public class GlobalNTS extends Production implements IGlobalNTS {
    private IToken ts_global;
    private ICpsDecl nts_cpsDecl;

    public GlobalNTS(IToken ts_global, ICpsDecl nts_cpsDecl) {
        this.ts_global = ts_global;
        this.nts_cpsDecl = nts_cpsDecl;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax() {
        return nts_cpsDecl.toAbsSyntax();
    }
}
