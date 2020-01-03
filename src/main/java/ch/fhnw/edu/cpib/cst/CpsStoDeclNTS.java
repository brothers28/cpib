package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
public class CpsStoDeclNTS extends Production implements ICpsStoDeclNTS {
    private IToken ts_semicolon;
    private IStoDecl nts_stoDecl;
    private ICpsStoDeclNTS nts_cpsStoDeclNTS;

    public CpsStoDeclNTS(IToken ts_semicolon, IStoDecl nts_stoDecl, ICpsStoDeclNTS nts_cpsStoDeclNTS) {
        this.ts_semicolon = ts_semicolon;
        this.nts_stoDecl = nts_stoDecl;
        this.nts_cpsStoDeclNTS = nts_cpsStoDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp) {
        temp.add(nts_stoDecl.toAbsSyntax());
        return nts_cpsStoDeclNTS.toAbsSyntax(temp);
    }
}
