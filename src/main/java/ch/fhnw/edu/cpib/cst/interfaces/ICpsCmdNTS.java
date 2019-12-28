package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.ICmd;

import java.util.ArrayList;

public interface ICpsCmdNTS extends IProduction {
    public ArrayList<ICmd> toAbsSyntax(ArrayList<ICmd> temp);
}
