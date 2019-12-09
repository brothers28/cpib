package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.StoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFunDeclNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// funDeclNTS ::= LOCAL cpsStoNTS
public class FunDeclNTS extends Production implements IFunDeclNTS {
    protected final Token T_local;
    protected final ICpsStoDecl N_cpsStoDecl;

    public FunDeclNTS(final Token T_local,
                      final ICpsStoDecl N_cpsStoDecl) {
        this.T_local = T_local;
        this.N_cpsStoDecl = N_cpsStoDecl;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn() {
        return N_cpsStoDecl.toAbsSyn();
    }
}
