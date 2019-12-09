package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IDecl;

import java.util.ArrayList;

// cpsDecl ::= decl cpsDeclNTS
public class CpsDecl extends Production implements ICpsDecl {
    protected final IDecl N_decl;
    protected final ICpsDeclNTS N_cpsDeclNTS;

    public CpsDecl(final IDecl N_decl,
                   final ICpsDeclNTS N_cpsDeclNTS) {
        this.N_decl = N_decl;
        this.N_cpsDeclNTS = N_cpsDeclNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> toAbsSyn() {
        ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> temp = new ArrayList<>();
        temp.add(N_decl.toAbsSyn());

        return N_cpsDeclNTS.toAbsSyn(temp);
    }
}
