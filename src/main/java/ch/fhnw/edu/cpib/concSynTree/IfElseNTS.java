package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.CpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

// ifelseNTS ::= ELSE cpsCmd
public class IfElseNTS extends Production implements IIfElseNTS {
    protected final Token T_else;
    protected final ICpsCmd N_cpsCmd;

    public IfElseNTS(final Token T_else,
                     final ICpsCmd N_cpsCmd) {
        this.T_else = T_else;
        this.N_cpsCmd = N_cpsCmd;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.CpsCmd toAbsSyn() {
        return N_cpsCmd.toAbsSyn();
    }
}
