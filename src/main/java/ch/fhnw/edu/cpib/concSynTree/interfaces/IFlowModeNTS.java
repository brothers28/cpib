package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;

public interface IFlowModeNTS extends IProduction {
    public Flowmodes toAbsSyn();
}
