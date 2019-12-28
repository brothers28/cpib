package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IFunDeclNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax();
}
