package ch.fhnw.edu.cpib.concSynTree.interfaces;

import java.util.ArrayList;

public interface ICpsStoDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> temp);
}
