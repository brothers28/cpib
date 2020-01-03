package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IProcDeclNTS extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax();
}
