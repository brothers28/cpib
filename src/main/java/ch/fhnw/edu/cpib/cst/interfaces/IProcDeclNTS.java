package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IProcDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax();
}
