package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmdNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
public class CpsCmdNTS extends Production implements ICpsCmdNTS {
    protected final IToken T_semicolon;
    protected final ICmd N_cmd;
    protected final ICpsCmdNTS N_cpsCmdNTS;

    public CpsCmdNTS(final IToken T_semicolon, final ICmd N_cmd, final ICpsCmdNTS N_cpsCmdNTS) {
        this.T_semicolon = T_semicolon;
        this.N_cmd = N_cmd;
        this.N_cpsCmdNTS = N_cpsCmdNTS;
    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> toAbsSyntax(
            ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> temp) {
        temp.add(N_cmd.toAbsSyntax());
        return N_cpsCmdNTS.toAbsSyntax(temp);
    }
}
