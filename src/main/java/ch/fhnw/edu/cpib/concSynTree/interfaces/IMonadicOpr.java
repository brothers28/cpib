package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.scanner.symbols.Operator;

public interface IMonadicOpr extends IProduction {
    Operator toAbsSyn();
}
