package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;

public interface IMechModeNTS extends IProduction {
    public Mechmodes toAbsSyn();
}
