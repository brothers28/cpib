package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IProgram;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
public class Program extends Production implements IProgram {
    protected IToken ts_program;
    protected IToken ts_ident;
    protected IGlobalNTS nts_globalNTS;
    protected IToken ts_do;
    protected ICpsCmd nts_cpsCmd;
    protected IToken ts_endprogram;

    public Program(IToken ts_program, IToken ts_ident, IGlobalNTS nts_globalNTS, IToken ts_do, ICpsCmd nts_cpsCmd,
            IToken ts_endprogram) {
        this.ts_program = ts_program;
        this.ts_ident = ts_ident;
        this.nts_globalNTS = nts_globalNTS;
        this.ts_do = ts_do;
        this.nts_cpsCmd = nts_cpsCmd;
        this.ts_endprogram = ts_endprogram;
    }

    @Override public ch.fhnw.edu.cpib.ast.Program toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.Program((Ident) ts_ident, nts_globalNTS.toAbsSyntax(),
                nts_cpsCmd.toAbsSyntax());
    }
}
