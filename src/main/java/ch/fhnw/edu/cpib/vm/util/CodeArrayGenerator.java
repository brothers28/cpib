package ch.fhnw.edu.cpib.vm.util;

import ch.fhnw.edu.cpib.ast.AstTree;
import ch.fhnw.edu.cpib.vm.ICodeArray;

public class CodeArrayGenerator {
    public ICodeArray convert(AstTree as) throws ICodeArray.CodeTooSmallError {
        return as.getCodeArray();
    }
}
