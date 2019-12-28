package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IFunDecl;

// decl ::= funDecl
public class DeclFunDecl extends Production implements IDecl {
    protected final IFunDecl N_funDecl;

    public DeclFunDecl(final IFunDecl N_funDecl) {
        this.N_funDecl = N_funDecl;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return N_funDecl.toAbsSyntax();
    }
}
