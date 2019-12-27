package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;

// stoDecl ::= typedIdent
public class StoDeclTypedIdent extends Production implements IStoDecl {
    protected final ITypedIdent N_typedIdent;

    public StoDeclTypedIdent(final ITypedIdent N_typedIdent) {
        this.N_typedIdent = N_typedIdent;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.StoDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.StoDecl(N_typedIdent.toAbsSyn());
    }
}
