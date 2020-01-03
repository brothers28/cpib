package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IParamNTS extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.Param> temp);
}

