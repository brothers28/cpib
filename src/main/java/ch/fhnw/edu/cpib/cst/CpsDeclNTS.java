package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
public class CpsDeclNTS extends Production implements ICpsDeclNTS {
    protected final IToken T_semicolon;
    protected final IDecl N_decl;
    protected final ICpsDeclNTS N_cpsDeclNTS;

    public CpsDeclNTS(final IToken t_semicolon, final IDecl n_decl, final ICpsDeclNTS n_cpsDeclNTS) {
        T_semicolon = t_semicolon;
        N_decl = n_decl;
        N_cpsDeclNTS = n_cpsDeclNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> temp) {
        temp.add(N_decl.toAbsSyntax());
        return N_cpsDeclNTS.toAbsSyntax(temp);
    }
}
