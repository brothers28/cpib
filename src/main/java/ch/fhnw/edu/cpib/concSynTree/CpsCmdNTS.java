package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmdNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
public class CpsCmdNTS extends Production implements ICpsCmdNTS {
    protected final Token T_semicolon;
    protected final ICmd N_cmd;
    protected final ICpsCmdNTS N_cpsCmdNTS;

    public CpsCmdNTS(final Token T_semicolon,
                     final ICmd N_cmd,
                     final ICpsCmdNTS N_cpsCmdNTS) {
        this.T_semicolon = T_semicolon;
        this.N_cmd = N_cmd;
        this.N_cpsCmdNTS = N_cpsCmdNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd> temp) {
        temp.add(N_cmd.toAbsSyn());
        return N_cpsCmdNTS.toAbsSyn(temp);
    }
}