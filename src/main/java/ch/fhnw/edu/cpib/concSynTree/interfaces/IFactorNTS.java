package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Ident;

public interface IFactorNTS extends IProduction {
    public IFactor toAbsSyn(Ident ident);
}
