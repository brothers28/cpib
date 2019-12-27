package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IFunDeclNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// funDeclNTS ::= LOCAL cpsStoNTS
public class FunDeclNTS extends Production implements IFunDeclNTS {
    protected final IToken T_local;
    protected final ICpsStoDecl N_cpsStoDecl;

    public FunDeclNTS(final IToken T_local,
                      final ICpsStoDecl N_cpsStoDecl) {
        this.T_local = T_local;
        this.N_cpsStoDecl = N_cpsStoDecl;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyn() {
        return N_cpsStoDecl.toAbsSyn();
    }
}
