package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IProgram;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
public class Program extends Production implements IProgram {
    protected final IToken T_program;
    protected final IToken T_ident;
    protected final IGlobalNTS N_globalNTS;
    protected final IToken T_do;
    protected final ICpsCmd N_cpsCmd;
    protected final IToken T_endprogram;

    public Program(final IToken t_program,
                   final IToken t_ident,
                   final IGlobalNTS n_globalNTS,
                   final IToken t_do,
                   final ICpsCmd n_cpsCmd,
                   final IToken t_endprogram) {
        T_program = t_program;
        T_ident = t_ident;
        N_globalNTS = n_globalNTS;
        T_do = t_do;
        N_cpsCmd = n_cpsCmd;
        T_endprogram = t_endprogram;
    }

    @Override
    public ch.fhnw.edu.cpib.ast.Program toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.Program((Ident) T_ident, N_globalNTS.toAbsSyn(), N_cpsCmd.toAbsSyn());
    }
}
