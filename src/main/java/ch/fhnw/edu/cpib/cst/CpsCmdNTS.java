package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmdNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
public class CpsCmdNTS extends Production implements ICpsCmdNTS {
    protected IToken ts_semicolon;
    protected ICmd nts_cmd;
    protected ICpsCmdNTS nts_cpsCmdNTS;

    public CpsCmdNTS(IToken ts_semicolon, ICmd nts_cmd, ICpsCmdNTS nts_cpsCmdNTS) {
        this.ts_semicolon = ts_semicolon;
        this.nts_cmd = nts_cmd;
        this.nts_cpsCmdNTS = nts_cpsCmdNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> toAbsSyntax(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> temp) {
        temp.add(nts_cmd.toAbsSyntax());
        return nts_cpsCmdNTS.toAbsSyntax(temp);
    }
}
