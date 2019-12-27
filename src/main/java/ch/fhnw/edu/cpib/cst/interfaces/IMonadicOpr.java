package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.scanner.symbols.Operator;

public interface IMonadicOpr extends IProduction {
    Operator toAbsSyn();
}
