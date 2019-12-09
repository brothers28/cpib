package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IProgram;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;

// program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
public class Program extends Production implements IProgram {
    protected final Token T_program;
    protected final Token T_ident;
    protected final IGlobalNTS N_globalNTS;
    protected final Token T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final Token T_endprogram;

    public Program(final Token t_program,
                   final Token t_ident,
                   final IGlobalNTS n_globalNTS,
                   final Token t_do,
                   final ICpsCmd n_cpsCmd,
                   final Token t_endprogram) {
        T_program = t_program;
        T_ident = t_ident;
        N_globalNTS = n_globalNTS;
        T_do = t_do;
        N_cpsCmd = n_cpsCmd;
        T_endprogram = t_endprogram;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.Program toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.Program((Ident) T_ident, N_globalNTS.toAbsSyn(), N_cpsCmd.toAbsSyn());
    }
}
