package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Ident;

public interface IFactorNTS extends IProduction {
    IFactor toAbsSyntax(Ident ident);
}
