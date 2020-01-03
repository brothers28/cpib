package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
public class CpsDeclNTS extends Production implements ICpsDeclNTS {
    protected final IToken ts_semicolon;
    protected final IDecl nts_decl;
    protected final ICpsDeclNTS nts_cpsDeclNTS;

    public CpsDeclNTS(final IToken ts_semicolon, final IDecl nts_decl, final ICpsDeclNTS nts_cpsDeclNTS) {
        this.ts_semicolon = ts_semicolon;
        this.nts_decl = nts_decl;
        this.nts_cpsDeclNTS = nts_cpsDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> temp) {
        temp.add(nts_decl.toAbsSyntax());
        return nts_cpsDeclNTS.toAbsSyntax(temp);
    }
}
