package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsStoDeclNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
public class CpsStoDeclNTS extends Production implements ICpsStoDeclNTS {
    protected final Token T_semicolon;
    protected final IStoDecl N_stoDecl;
    protected final ICpsStoDeclNTS N_cpsStoDeclNTS;

    public CpsStoDeclNTS(final Token t_semicolon,
                         final IStoDecl n_stoDecl,
                         final ICpsStoDeclNTS n_cpsStoDeclNTS) {
        T_semicolon = t_semicolon;
        N_stoDecl = n_stoDecl;
        N_cpsStoDeclNTS = n_cpsStoDeclNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> temp) {
        temp.add(N_stoDecl.toAbsSyn());
        return N_cpsStoDeclNTS.toAbsSyn(temp);
    }
}
