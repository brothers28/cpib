package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IProcDecl;

// decl ::= procDecl
public class DeclProc extends Production implements IDecl {
    protected IProcDecl nts_procDecl;

    public DeclProc(IProcDecl nts_procDecl) {
        this.nts_procDecl = nts_procDecl;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IDecl toAbsSyntax() {
        return nts_procDecl.toAbsSyntax();
    }
}
