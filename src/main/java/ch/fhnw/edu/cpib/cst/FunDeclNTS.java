package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.IFunDeclNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// funDeclNTS ::= LOCAL cpsStoNTS
public class FunDeclNTS extends Production implements IFunDeclNTS {
    private IToken ts_local;
    private ICpsStoDecl nts_cpsStoDecl;

    public FunDeclNTS(IToken ts_local, ICpsStoDecl nts_cpsStoDecl) {
        this.ts_local = ts_local;
        this.nts_cpsStoDecl = nts_cpsStoDecl;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax() {
        return nts_cpsStoDecl.toAbsSyntax();
    }
}
