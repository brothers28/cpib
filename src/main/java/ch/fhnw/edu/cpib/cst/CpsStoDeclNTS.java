package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
public class CpsStoDeclNTS extends Production implements ICpsStoDeclNTS {
    protected final IToken T_semicolon;
    protected final IStoDecl N_stoDecl;
    protected final ICpsStoDeclNTS N_cpsStoDeclNTS;

    public CpsStoDeclNTS(final IToken t_semicolon, final IStoDecl n_stoDecl, final ICpsStoDeclNTS n_cpsStoDeclNTS) {
        T_semicolon = t_semicolon;
        N_stoDecl = n_stoDecl;
        N_cpsStoDeclNTS = n_cpsStoDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp) {
        temp.add(N_stoDecl.toAbsSyntax());
        return N_cpsStoDeclNTS.toAbsSyntax(temp);
    }
}
