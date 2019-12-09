package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd;

import java.util.ArrayList;

public interface ICpsCmdNTS extends IProduction {
    public ArrayList<ICmd> toAbsSyn(ArrayList<ICmd> temp);
}
