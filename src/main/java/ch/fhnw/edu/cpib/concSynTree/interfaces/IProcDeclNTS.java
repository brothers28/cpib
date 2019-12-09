package ch.fhnw.edu.cpib.concSynTree.interfaces;

import java.util.ArrayList;

public interface IProcDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn();
}
