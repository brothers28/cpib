package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;

import java.util.ArrayList;

public interface IGlobalNTS extends IProduction {
    public ArrayList<IDecl> toAbsSyn();
}
