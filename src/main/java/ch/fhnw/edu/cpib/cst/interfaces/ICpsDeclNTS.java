package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;

import java.util.ArrayList;

public interface ICpsDeclNTS extends IProduction {
    public ArrayList<IDecl> toAbsSyn(ArrayList<IDecl> temp);
}
