package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IParamList extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax();
}
