package ch.fhnw.edu.cpib.vm.util;

import ch.fhnw.edu.cpib.absSynTree.AbsSynTree;
import ch.fhnw.edu.cpib.vm.ICodeArray;

public class CodeArrayGenerator {
    public ICodeArray convert(AbsSynTree as) throws ICodeArray.CodeTooSmallError {
        return as.getCodeArray();
    }
}
