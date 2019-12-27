package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;

public interface IFlowModeNTS extends IProduction {
    public Flowmodes toAbsSyn();
}
