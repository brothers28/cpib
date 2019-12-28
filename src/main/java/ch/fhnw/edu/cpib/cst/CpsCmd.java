package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmdNTS;

import java.util.ArrayList;

// cpsCmd ::= cmd cpsCmdNTS
public class CpsCmd extends Production implements ICpsCmd {
    protected final ICmd N_cmd;
    protected final ICpsCmdNTS N_cpsCmdNTS;

    public CpsCmd(final ICmd N_cmd, final ICpsCmdNTS N_cpsCmdNTS) {
        this.N_cmd = N_cmd;
        this.N_cpsCmdNTS = N_cpsCmdNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> temp = new ArrayList<>();
        temp.add(N_cmd.toAbsSyntax());

        return new ch.fhnw.edu.cpib.ast.CpsCmd(N_cpsCmdNTS.toAbsSyntax(temp));
    }
}
