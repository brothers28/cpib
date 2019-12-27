package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// globalNTS ::= GLOBAL cpsDecl
public class GlobalNTS extends Production implements IGlobalNTS {
    protected final IToken T_global;
    protected final ICpsDecl N_cpsDecl;

    public GlobalNTS(final IToken t_global,
                     final ICpsDecl n_cpsDecl) {
        T_global = t_global;
        N_cpsDecl = n_cpsDecl;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyn() {
        return N_cpsDecl.toAbsSyn();
    }
}
