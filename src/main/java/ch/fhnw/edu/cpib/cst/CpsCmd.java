package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmdNTS;

import java.util.ArrayList;

// cpsCmd ::= cmd cpsCmdNTS
public class CpsCmd extends Production implements ICpsCmd {
    protected ICmd nts_cmd;
    protected ICpsCmdNTS nts_cpsCmdNTS;

    public CpsCmd(ICmd nts_cmd, ICpsCmdNTS nts_cpsCmdNTS) {
        this.nts_cmd = nts_cmd;
        this.nts_cpsCmdNTS = nts_cpsCmdNTS;
    }

    @Override public ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> temp = new ArrayList<>();
        temp.add(nts_cmd.toAbsSyntax());

        return new ch.fhnw.edu.cpib.ast.CpsCmd(nts_cpsCmdNTS.toAbsSyntax(temp));
    }
}
