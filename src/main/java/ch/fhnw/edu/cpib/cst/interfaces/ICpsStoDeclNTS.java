package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface ICpsStoDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp);
}
