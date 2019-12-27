package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;

// decl ::= stoDecl
public class DeclStoDecl extends Production implements IDecl {
    protected final IStoDecl N_stoDecl;

    public DeclStoDecl(final IStoDecl N_stoDecl) {
        this.N_stoDecl = N_stoDecl;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyn() {
        return N_stoDecl.toAbsSyn();
    }
}
