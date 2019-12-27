package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;

public interface IMechModeNTS extends IProduction {
    public Mechmodes toAbsSyn();
}
