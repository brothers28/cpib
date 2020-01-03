package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IParamListNTS extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax();
}
