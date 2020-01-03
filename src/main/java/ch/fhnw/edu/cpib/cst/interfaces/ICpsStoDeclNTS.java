package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface ICpsStoDeclNTS extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> temp);
}
