package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IFunDecl;

// decl ::= funDecl
public class DeclFun extends Production implements IDecl {
    private IFunDecl nts_funDecl;

    public DeclFun(IFunDecl nts_funDecl) {
        this.nts_funDecl = nts_funDecl;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return nts_funDecl.toAbsSyntax();
    }
}
