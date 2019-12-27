package ch.fhnw.edu.cpib.ast;

import ch.fhnw.edu.cpib.ast.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Ident;

public abstract class IdentFactor extends AstNode implements IFactor {
    protected Ident ident;
}
