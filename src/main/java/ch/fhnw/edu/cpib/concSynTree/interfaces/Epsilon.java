package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.*;
import ch.fhnw.edu.cpib.absSynTree.CpsCmd;
import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.absSynTree.Param;
import ch.fhnw.edu.cpib.concSynTree.Production;
import ch.fhnw.edu.cpib.concSynTree.interfaces.*;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.scanner.Ident;

import java.util.ArrayList;

public interface Epsilon {
    class GlobalNTS extends Production implements IGlobalNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> toAbsSyn() {
            return new ArrayList<>();
        }
    }

    class CpsDeclNTS extends Production implements ICpsDeclNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> toAbsSyn(ArrayList<IDecl> temp) {
            return temp;
        }
    }

    class FunDeclNTS extends Production implements IFunDeclNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn() {
            return new ArrayList<>();
        }
    }

    class ParamListNTS extends Production implements IParamListNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn() {
            return new ArrayList<>();
        }
    }

    class ParamNTS extends Production implements IParamNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> temp) {
            return temp;
        }
    }

    class FlowModeNTS extends Production implements IFlowModeNTS {
        @Override
        public Flowmodes toAbsSyn() {
            return null;
        }
    }

    class ChangeModeNTS extends Production implements IChangeModeNTS {
        @Override
        public Changemodes toAbySyn() {
            return null;
        }
    }

    class MechModeNTS extends Production implements IMechModeNTS {
        @Override
        public Mechmodes toAbsSyn() {
            return null;
        }
    }

    class CpsCmdNTS extends Production implements ICpsCmdNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd> temp) {
            return temp;
        }
    }

    class IfElseNTS extends Production implements IIfElseNTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.CpsCmd toAbsSyn() {
            return null;
        }
    }

    class ExprNTS extends Production implements IExprNTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term1NTS extends Production implements ITerm1NTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term2NTS extends Production implements ITerm2NTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term3NTS extends Production implements ITerm3NTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr toAbsSyn(ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr expr) {
            return expr;
        }
    }

    class FactorNTS extends Production implements IFactorNTS {
        @Override
        public ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor toAbsSyn(Ident ident) {
            return new ch.fhnw.edu.cpib.absSynTree.InitFactor(ident, false);
        }
    }

    class ExprListLParenNTS extends Production implements IExprListLparenNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> toAbsSyn() {
            return new ArrayList<>();
        }
    }

    class ExprListNTS extends Production implements IExprListNTS {
        @Override
        public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr> temp) {
            return temp;
        }
    }

    class CpsStoDeclNTS extends Production implements ICpsStoDeclNTS {
        @Override
        public ArrayList<StoDecl> toAbsSyn(ArrayList<StoDecl> temp) {
            return temp;
        }
    }

    class ProcDeclNTS extends Production implements IProcDeclNTS {
        @Override
        public ArrayList<StoDecl> toAbsSyn() {
            return new ArrayList<>();
        }
    }
}
