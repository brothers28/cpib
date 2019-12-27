package ch.fhnw.edu.cpib.vm.util;

import ch.fhnw.edu.cpib.ast.AbsSynTree;
import ch.fhnw.edu.cpib.vm.ICodeArray;

public class CodeArrayGenerator {
    public ICodeArray convert(AbsSynTree as) throws ICodeArray.CodeTooSmallError {
        return as.getCodeArray();
    }
}
