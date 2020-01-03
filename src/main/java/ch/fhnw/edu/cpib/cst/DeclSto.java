package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;

// decl ::= stoDecl
public class DeclSto extends Production implements IDecl {
    protected IStoDecl nts_stoDecl;

    public DeclSto(IStoDecl nts_stoDecl) {
        this.nts_stoDecl = nts_stoDecl;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return nts_stoDecl.toAbsSyntax();
    }
}
