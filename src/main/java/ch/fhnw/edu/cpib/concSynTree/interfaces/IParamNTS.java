package ch.fhnw.edu.cpib.concSynTree.interfaces;

import java.util.ArrayList;

public interface IParamNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> temp);
}

