package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

public interface ICastOpr extends IProduction {
    Types toAbsSyn();
}
