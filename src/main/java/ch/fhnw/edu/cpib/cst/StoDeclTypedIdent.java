package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;

// stoDecl ::= typedIdent
public class StoDeclTypedIdent extends Production implements IStoDecl {
    private ITypedIdent nts_typedIdent;

    public StoDeclTypedIdent(ITypedIdent nts_typedIdent) {
        this.nts_typedIdent = nts_typedIdent;
    }

    @Override public ch.fhnw.edu.cpib.ast.StoDecl toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.StoDecl(nts_typedIdent.toAbsSyntax());
    }
}
