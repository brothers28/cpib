package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmdNTS;

import java.util.ArrayList;

// cpsCmd ::= cmd cpsCmdNTS
public class CpsCmd extends Production implements ICpsCmd {
    protected final ICmd N_cmd;
    protected final ICpsCmdNTS N_cpsCmdNTS;

    public CpsCmd(final ICmd N_cmd,
                  final ICpsCmdNTS N_cpsCmdNTS) {
        this.N_cmd = N_cmd;
        this.N_cpsCmdNTS = N_cpsCmdNTS;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.CpsCmd toAbsSyn() {
        ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd> temp = new ArrayList<>();
        temp.add(N_cmd.toAbsSyn());

        return new ch.fhnw.edu.cpib.absSynTree.CpsCmd(N_cpsCmdNTS.toAbsSyn(temp));
    }
}