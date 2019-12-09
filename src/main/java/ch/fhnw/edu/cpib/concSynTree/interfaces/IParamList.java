package ch.fhnw.edu.cpib.concSynTree.interfaces;

import java.util.ArrayList;

public interface IParamList extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn();
}
