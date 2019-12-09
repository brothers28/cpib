package ch.fhnw.edu.cpib.concSynTree.interfaces;

import java.util.ArrayList;

public interface IFunDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn();
}
