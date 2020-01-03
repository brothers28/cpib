package ch.fhnw.edu.cpib.parser;

import ch.fhnw.edu.cpib.ast.AstTree;
import ch.fhnw.edu.cpib.cst.*;
import ch.fhnw.edu.cpib.cst.interfaces.*;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.TokenList;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

public class Parser {
    private TokenList tokens;
    private Base currentToken;
    private Terminals currentTerminal;
    private int counter;

    public Parser(TokenList tokens) {
        // Setup token processing
        this.tokens = tokens;
        nextToken();
    }

    private void nextToken() {
        currentToken = (Base) tokens.get(counter++);
        currentTerminal = currentToken.getTerminal();
    }

    private IToken consume(Terminals expectedTerminal) throws GrammarError {
        if (this.currentTerminal == expectedTerminal) {
            IToken consumedTerminal = this.currentToken;
            if (this.currentTerminal != Terminals.SENTINEL) {
                nextToken();
            }
            return consumedTerminal;
        } else {
            throw new GrammarError(expectedTerminal, this.currentToken.getTerminal());
        }
    }

    public AstTree parse()
            throws GrammarError, AlreadyDeclaredError, NotDeclaredError, AlreadyGloballyDeclaredError, LRValError,
            InvalidParamCountError, AlreadyInitializedError, TypeCheckError, NotInitializedError,
            GlobalProtectedInitializationError, AssignToConstError, CastError {
        System.out.println("Start parsing:\n");
        IProgram program = program();
        consume(Terminals.SENTINEL);

        System.out.println("\n---------------------------------------------------\n");
        System.out.println("Concrete Syntax Tree:\n");
        System.out.println(program.toString(""));

        System.out.println("\n---------------------------------------------------\n");
        System.out.println("Abstract Syntax Tree:\n");
        AstTree ast = new AstTree(program);
        System.out.println(ast.toString());

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Scope checking:");
        ast.executeScopeCheck();
        System.out.println(" OK!");

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Type checking:");
        ast.executeTypeCheck();
        System.out.println(" OK!");

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Init checking:");
        ast.executeInitCheck();
        System.out.println(" OK!");

        return ast;
    }

    // program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
    // TODO: progParamList fehlt hier
    private IProgram program() throws GrammarError {
        if (currentTerminal == Terminals.PROGRAM) {
            System.out.println("program ::= PROGRAM IDENT <globalNTS> DO <cpsCmd> ENDPROGRAM");
            IToken ts_program = consume(Terminals.PROGRAM);
            IToken ts_ident = consume(Terminals.IDENT);
            IGlobalNTS nts_globalNTS = globalNTS();
            IToken ts_do = consume(Terminals.DO);
            ICpsCmd nts_cpsCmd = cpsCmd();
            IToken ts_endprogram = consume(Terminals.ENDPROGRAM);
            return new Program(ts_program, ts_ident, nts_globalNTS, ts_do, nts_cpsCmd, ts_endprogram);
        } else {
            throw new GrammarError(Terminals.PROGRAM, currentTerminal);
        }
    }

    // globalNTS ::= GLOBAL cpsDecl
    // globalNTS ::= ε
    private IGlobalNTS globalNTS() throws GrammarError {
        if (currentTerminal == Terminals.GLOBAL) {
            System.out.println("globalNTS ::= GLOBAL <cpsDecl>");
            IToken ts_global = consume(Terminals.GLOBAL);
            ICpsDecl nts_cpsDecl = cpsDecl();
            return new GlobalNTS(ts_global, nts_cpsDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("globalNTS ::= ε");
            return new IEpsilon.GlobalNTS();
        } else {
            throw new GrammarError(Terminals.GLOBALNTS, currentTerminal);
        }
    }

    // cpsDecl ::= decl cpsDeclNTS
    private ICpsDecl cpsDecl() throws GrammarError {
        if (currentTerminal == Terminals.PROC || currentTerminal == Terminals.FUN
                || currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("cpsDecl ::= <decl> <cpsDeclNTS>");
            IDecl nts_decl = decl();
            ICpsDeclNTS nts_cpsDeclNTS = cpsDeclNTS();
            return new CpsDecl(nts_decl, nts_cpsDeclNTS);
        } else {
            throw new GrammarError(Terminals.CPSDECL, currentTerminal);
        }
    }

    // cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
    // cpsDeclNTS ::= ε
    private ICpsDeclNTS cpsDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsDeclNTS ::= SEMICOLON <decl> <cpsDeclNTS>");
            IToken ts_semicolon = consume(Terminals.SEMICOLON);
            IDecl nts_decl = decl();
            ICpsDeclNTS nts_cpsDeclNTS = cpsDeclNTS();
            return new CpsDeclNTS(ts_semicolon, nts_decl, nts_cpsDeclNTS);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("cpsDeclNTS ::= ε");
            return new IEpsilon.CpsDeclNTS();
        } else {
            throw new GrammarError(Terminals.CPSDECLNTS, currentTerminal);
        }
    }

    // decl ::= stoDecl
    // decl ::= funDecl
    // decl ::= procDecl
    private IDecl decl() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("decl ::= <stoDecl>");
            IStoDecl nts_stoDecl = stoDecl();
            return new DeclSto(nts_stoDecl);
        } else if (currentTerminal == Terminals.FUN) {
            System.out.println("decl ::= <funDecl>");
            IFunDecl nts_funDecl = funDecl();
            return new DeclFun(nts_funDecl);
        } else if (currentTerminal == Terminals.PROC) {
            System.out.println("decl ::= <procDecl>");
            IProcDecl nts_procDecl = procDecl();
            return new DeclProc(nts_procDecl);
        } else {
            throw new GrammarError(Terminals.DECL, currentTerminal);
        }
    }

    // stoDecl ::= typedIdent
    // stoDecl ::= CHANGEMOD typedIdent
    private IStoDecl stoDecl() throws GrammarError {
        if (currentTerminal == Terminals.IDENT) {
            System.out.println("stoDecl ::= <typedIdent>");
            ITypedIdent nts_typedIdent = typedIdent();
            return new StoDeclTypedIdent(nts_typedIdent);
        } else if (currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("stoDecl ::= CHANGEMOD <typedIdent>");
            IToken ts_changeMode = consume(Terminals.CHANGEMOD);
            ITypedIdent nts_typedIdent = typedIdent();
            return new StoDeclChangeMode(ts_changeMode, nts_typedIdent);
        } else {
            throw new GrammarError(Terminals.STODECL, currentTerminal);
        }
    }

    // typedIdent ::= IDENT COLON TYPE
    private ITypedIdent typedIdent() throws GrammarError {
        if (currentTerminal == Terminals.IDENT) {
            System.out.println("typedIdent ::= IDENT COLON TYPE");
            IToken ts_ident = consume(Terminals.IDENT);
            IToken ts_colon = consume(Terminals.COLON);
            IToken ts_type = consume(Terminals.TYPE);
            return new TypedIdent(ts_ident, ts_colon, ts_type);
        } else {
            throw new GrammarError(Terminals.TYPEDIDENT, currentTerminal);
        }
    }

    // funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
    private IFunDecl funDecl() throws GrammarError {
        if (currentTerminal == Terminals.FUN) {
            System.out.println("funDecl ::= FUN IDENT <paramList> RETURNS <stoDecl> <funDeclNTS> DO <cpsCmd> ENDFUN");
            IToken ts_fun = consume(Terminals.FUN);
            IToken ts_ident = consume(Terminals.IDENT);
            IParamList nts_paramList = paramList();
            IToken ts_returns = consume(Terminals.RETURNS);
            IStoDecl nts_stoDecl = stoDecl();
            IFunDeclNTS nts_funDeclNTS = funDeclNTS();
            IToken ts_do = consume(Terminals.DO);
            ICpsCmd nts_cpsCmd = cpsCmd();
            IToken ts_endFun = consume(Terminals.ENDFUN);
            return new FunDecl(ts_fun, ts_ident, nts_paramList, ts_returns, nts_stoDecl, nts_funDeclNTS, ts_do, nts_cpsCmd,
                    ts_endFun);
        } else {
            throw new GrammarError(Terminals.FUNDECL, currentTerminal);
        }
    }

    // funDeclNTS ::= LOCAL cpsStoNTS
    // funDeclNTS ::= ε
    private IFunDeclNTS funDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.LOCAL) {
            System.out.println("funDeclNTS := LOCAL <cpsStoDecl>");
            IToken ts_local = consume(Terminals.LOCAL);
            ICpsStoDecl nts_cpsStoDecl = cpsStoDecl();
            return new FunDeclNTS(ts_local, nts_cpsStoDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("funDeclNTS := ε");
            return new IEpsilon.FunDeclNTS();
        } else {
            throw new GrammarError(Terminals.FUNDECLNTS, currentTerminal);
        }
    }

    // paramList ::= LPAREN paramListNTS RPAREN
    private IParamList paramList() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN) {
            System.out.println("paramList ::= LPAREN <paramListNTS> RPAREN");
            IToken ts_lParen = consume(Terminals.LPAREN);
            IParamListNTS nts_paramListNTS = paramListNTS();
            IToken ts_rParen = consume(Terminals.RPAREN);
            return new ParamList(ts_lParen, nts_paramListNTS, ts_rParen);
        } else {
            throw new GrammarError(Terminals.PARAMLIST, currentTerminal);
        }
    }

    // paramListNTS ::= param paramNTS
    // paramListNTS ::= ε
    private IParamListNTS paramListNTS() throws GrammarError {
        if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.MECHMODE
                || currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("paramListNTS ::= <param> <paramNTS>");
            IParam nts_param = param();
            IParamNTS nts_paramNTS = paramNTS();
            return new ParamListNTS(nts_param, nts_paramNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("paramListNTS ::= ε");
            return new IEpsilon.ParamListNTS();
        } else {
            throw new GrammarError(Terminals.PARAMLISTNTS, currentTerminal);
        }
    }

    // paramNTS ::= COMMA param paramNTS
    // paramNTS ::= ε
    private IParamNTS paramNTS() throws GrammarError {
        if (currentTerminal == Terminals.COMMA) {
            System.out.println("paramNTS ::= COMMA <param> <paramNTS>");
            IToken ts_comma = consume(Terminals.COMMA);
            IParam nts_param = param();
            IParamNTS nts_paramNTS = paramNTS();
            return new ParamNTS(ts_comma, nts_param, nts_paramNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("paramNTS ::= ε");
            return new IEpsilon.ParamNTS();
        } else {
            throw new GrammarError(Terminals.PARAMNTS, currentTerminal);
        }
    }

    // param ::= flowModeNTS mechModeNTS changeModNTS typedIdent
    private IParam param() throws GrammarError {
        if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.FLOWMODE
                || currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.MECHMODE) {
            System.out.println("param ::= <flowModeNTS> <mechModeNTS> <changeModNTS> <typedIdent>");
            IFlowModeNTS nts_flowModeNTS = flowModeNTS();
            IMechModeNTS nts_mechModeNTS = mechModeNTS();
            IChangeModeNTS nts_changeModeNTS = changeModNTS();
            ITypedIdent nts_typedIdent = typedIdent();
            return new Param(nts_flowModeNTS, nts_mechModeNTS, nts_changeModeNTS, nts_typedIdent);
        } else {
            throw new GrammarError(Terminals.PARAM, currentTerminal);
        }
    }

    // flowModeNTS ::= FLOWMODE
    // floModeNTS ::= ε
    private IFlowModeNTS flowModeNTS() throws GrammarError {
        if (currentTerminal == Terminals.FLOWMODE) {
            System.out.println("flowModeNTS ::= FLOWMODE");
            IToken ts_flowMode = consume(Terminals.FLOWMODE);
            return new FlowModeNTS(ts_flowMode);
        } else if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.MECHMODE
                || currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("flowModeNTS ::= ε");
            return new IEpsilon.FlowModeNTS();
        } else {
            throw new GrammarError(Terminals.FLOWMODENTS, currentTerminal);
        }
    }

    // changeModNTS ::= CHANGEMOD
    // changeModNTS::= ε
    private IChangeModeNTS changeModNTS() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("changeModNTS ::= CHANGEMOD");
            IToken ts_changeMode = consume(Terminals.CHANGEMOD);
            return new ChangeModeNTS(ts_changeMode);
        } else if (currentTerminal == Terminals.IDENT) {
            System.out.println("changeModNTS ::= ε");
            return new IEpsilon.ChangeModeNTS();
        } else {
            throw new GrammarError(Terminals.CHANGEMODENTS, currentTerminal);
        }
    }

    // mechModeNTS ::= MECHMODE
    // mechModeNTS::= ε
    private IMechModeNTS mechModeNTS() throws GrammarError {
        if (currentTerminal == Terminals.MECHMODE) {
            System.out.println("mechModeNTS ::= MECHMODE");
            IToken ts_mechMode = consume(Terminals.MECHMODE);
            return new MechModeNTS(ts_mechMode);
        } else if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("mechModeNTS ::= ε");
            return new IEpsilon.MechModeNTS();
        } else {
            throw new GrammarError(Terminals.MECHMODENTS, currentTerminal);
        }
    }

    // cpsCmd ::= cmd cpsCmdNTS
    private ICpsCmd cpsCmd() throws GrammarError {
        if (currentTerminal == Terminals.DEBUGOUT || currentTerminal == Terminals.DEBUGIN
                || currentTerminal == Terminals.CALL || currentTerminal == Terminals.WHILE
                || currentTerminal == Terminals.IF || currentTerminal == Terminals.LPAREN
                || currentTerminal == Terminals.ADDOPR || currentTerminal == Terminals.NOTOPR
                || currentTerminal == Terminals.IDENT || currentTerminal == Terminals.LITERAL
                || currentTerminal == Terminals.SKIP) {
            System.out.println("cpsCMD ::= <cmd> <cpsCmdNTS>");
            ICmd nts_cmd = cmd();
            ICpsCmdNTS nts_cpsCmdNTS = cpsCmdNTS();
            return new CpsCmd(nts_cmd, nts_cpsCmdNTS);
        } else {
            throw new GrammarError(Terminals.CPSCMD, currentTerminal);
        }
    }

    // cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
    // cpsCmdNTS ::= ε
    private ICpsCmdNTS cpsCmdNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsCmdNTS ::= SEMICOLON <cmd> <cpsCmdNTS>");
            IToken ts_semicolon = consume(Terminals.SEMICOLON);
            ICmd nts_cmd = cmd();
            ICpsCmdNTS nts_cpsCmdNTS = cpsCmdNTS();
            return new CpsCmdNTS(ts_semicolon, nts_cmd, nts_cpsCmdNTS);
        } else if (currentTerminal == Terminals.ENDPROC || currentTerminal == Terminals.ENDWHILE
                || currentTerminal == Terminals.ENDIF || currentTerminal == Terminals.ELSE
                || currentTerminal == Terminals.ENDFUN || currentTerminal == Terminals.ENDPROGRAM) {
            System.out.println("cpsCmdNTS ::= ε");
            return new IEpsilon.CpsCmdNTS();
        } else {
            throw new GrammarError(Terminals.CPSCMDNTS, currentTerminal);
        }
    }

    // cmd ::= SKIP
    // cmd ::= expr BECOMES expr
    // cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
    // cmd ::= WHILE expr DO cpsCmd ENDWHILE
    // cmd ::= CALL IDENT exprList
    // cmd ::= DEBUGIN expr
    // cmd ::= DEBUGOUT expr
    private ICmd cmd() throws GrammarError {
        if (currentTerminal == Terminals.SKIP) {
            System.out.println("cmd ::= SKIP");
            IToken ts_skip = consume(Terminals.SKIP);
            return new CmdSkip(ts_skip);
        } else if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("cmd ::= <expr> BECOMES <expr>");
            IExpr nts_expr1 = expr();
            IToken ts_becomes = consume(Terminals.BECOMES);
            IExpr nts_expr2 = expr();
            return new CmdExpr(nts_expr1, ts_becomes, nts_expr2);
        } else if (currentTerminal == Terminals.IF) {
            System.out.println("cmd ::= IF <expr> THEN <cpsCmd> <ifelseNTS> ENDIF");
            IToken ts_if = consume(Terminals.IF);
            IExpr nts_expr = expr();
            IToken ts_then = consume(Terminals.THEN);
            ICpsCmd nts_cpsCmd = cpsCmd();
            IIfElseNTS nts_ifelseNTS = ifElseNTS();
            IToken ts_endif = consume(Terminals.ENDIF);
            return new CmdIf(ts_if, nts_expr, ts_then, nts_cpsCmd, nts_ifelseNTS, ts_endif);
        } else if (currentTerminal == Terminals.WHILE) {
            System.out.println("cmd ::= WHILE <expr> DO <cpsCmd> ENDWHILE");
            IToken ts_while = consume(Terminals.WHILE);
            IExpr nts_expr = expr();
            IToken ts_do = consume(Terminals.DO);
            ICpsCmd nts_cpsCmd = cpsCmd();
            IToken ts_endwhile = consume(Terminals.ENDWHILE);
            return new CmdWhile(ts_while, nts_expr, ts_do, nts_cpsCmd, ts_endwhile);
        } else if (currentTerminal == Terminals.CALL) {
            System.out.println("cmd ::= CALL IDENT <exprList>");
            IToken ts_call = consume(Terminals.CALL);
            IToken ts_ident = consume(Terminals.IDENT);
            IExprList nts_exprList = exprList();
            return new CmdCall(ts_call, ts_ident, nts_exprList);
        } else if (currentTerminal == Terminals.DEBUGIN) {
            System.out.println("cmd ::= DEBUGIN <expr>");
            IToken ts_debugIn = consume(Terminals.DEBUGIN);
            IExpr nts_expr = expr();
            return new CmdDebugIn(ts_debugIn, nts_expr);
        } else if (currentTerminal == Terminals.DEBUGOUT) {
            System.out.println("cmd ::= DEBUGOUT <expr>");
            IToken ts_debugOut = consume(Terminals.DEBUGOUT);
            IExpr nts_expr = expr();
            return new CmdDebugOut(ts_debugOut, nts_expr);
        } else {
            throw new GrammarError(Terminals.CMD, currentTerminal);
        }
    }

    // ifelseNTS ::= ELSE cpsCmd
    // ifelseNTS ::= ε
    private IIfElseNTS ifElseNTS() throws GrammarError {
        if (currentTerminal == Terminals.ELSE) {
            System.out.println("ifelseNTS ::= ELSE <cpsCmd>");
            IToken ts_else = consume(Terminals.ELSE);
            ICpsCmd nts_cpsCmd = cpsCmd();
            return new IfElseNTS(ts_else, nts_cpsCmd);
        } else if (currentTerminal == Terminals.ENDIF) {
            System.out.println("ifelseNTS ::= ε");
            return new IEpsilon.IfElseNTS();
        } else {
            throw new GrammarError(Terminals.IFELSENTS, currentTerminal);
        }
    }

    // expr ::= term1 exprNTS
    private IExpr expr() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL || currentTerminal == Terminals.LBRACKET) {
            System.out.println("expr ::= <term1> <exprNTS>");
            ITerm1 nts_term1 = term1();
            IExprNTS nts_exprNTS = exprNTS();
            return new Expr(nts_term1, nts_exprNTS);
        } else {
            throw new GrammarError(Terminals.EXPR, currentTerminal);
        }
    }

    // exprNTS ::= BOOLOPR term1 exprNTS
    // exprNTS ::= ε
    private IExprNTS exprNTS() throws GrammarError {
        if (currentTerminal == Terminals.BOOLOPR) {
            System.out.println("exprNTS ::= BOOLOPR <term1> <exprNTS>");
            IToken ts_boolOpr = consume(Terminals.BOOLOPR);
            ITerm1 nts_term1 = term1();
            IExprNTS nts_exprNTS = exprNTS();
            return new ExprNTS(ts_boolOpr, nts_term1, nts_exprNTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES) {
            System.out.println("exprNTS ::= ε");
            return new IEpsilon.ExprNTS();
        } else {
            throw new GrammarError(Terminals.EXPRNTS, currentTerminal);
        }
    }

    // term1 ::= term2 term1NTS
    private ITerm1 term1() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL || currentTerminal == Terminals.LBRACKET) {
            System.out.println("term1 ::= <term2> <term1NTS>");
            ITerm2 nts_term2 = term2();
            ITerm1NTS nts_term1NTS = term1NTS();
            return new Term1(nts_term2, nts_term1NTS);
        } else {
            throw new GrammarError(Terminals.TERM1, currentTerminal);
        }
    }

    // term1NTS ::= RELOPR term2 term1NTS
    // term1NTS ::= ε
    private ITerm1NTS term1NTS() throws GrammarError {
        if (currentTerminal == Terminals.RELOPR) {
            System.out.println("term1NTS ::= RELOPR <term2> <term1NTS>");
            IToken ts_relOpr = consume(Terminals.RELOPR);
            ITerm2 nts_term2 = term2();
            ITerm1NTS nts_term1NTS = term1NTS();
            return new Term1NTS(ts_relOpr, nts_term2, nts_term1NTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR) {
            System.out.println("term1NTS ::= ε");
            return new IEpsilon.Term1NTS();
        } else {
            throw new GrammarError(Terminals.TERM1NTS, currentTerminal);
        }
    }

    // term2 ::= term3 term2NTS
    private ITerm2 term2() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL || currentTerminal == Terminals.LBRACKET) {
            System.out.println("term2 ::= <term3> <term2NTS>");
            ITerm3 nts_term3 = term3();
            ITerm2NTS nts_term2NTS = term2NTS();
            return new Term2(nts_term3, nts_term2NTS);
        } else {
            throw new GrammarError(Terminals.TERM2, currentTerminal);
        }
    }

    // term2NTS ::= ADDOPR term3 term2NTS
    // term2NTS ::= ε
    private ITerm2NTS term2NTS() throws GrammarError {
        if (currentTerminal == Terminals.ADDOPR) {
            System.out.println("term2NTS ::= ADDOPR <term3> <term2NTS>");
            IToken ts_addOpr = consume(Terminals.ADDOPR);
            ITerm3 nts_term3 = term3();
            ITerm2NTS nts_term2NTS = term2NTS();
            return new Term2NTS(ts_addOpr, nts_term3, nts_term2NTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR) {
            System.out.println("term2NTS ::= ε");
            return new IEpsilon.Term2NTS();
        } else {
            throw new GrammarError(Terminals.TERM2NTS, currentTerminal);
        }
    }

    // term3 ::= factor term3NTS
    private ITerm3 term3() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL || currentTerminal == Terminals.LBRACKET) {
            System.out.println("term3 ::= <factor> <term3NTS>");
            IFactor nts_factor = factor();
            ITerm3NTS nts_term3NTS = term3NTS();
            return new Term3(nts_factor, nts_term3NTS);
        } else {
            throw new GrammarError(Terminals.TERM3, currentTerminal);
        }
    }

    // term3NTS ::= MULTOPR factor term3NTS
    // term3NTS ::= ε
    private ITerm3NTS term3NTS() throws GrammarError {
        if (currentTerminal == Terminals.MULTOPR) {
            System.out.println("term3NTS ::= MULTOPR <factor> <term3NTS>");
            IToken ts_multOpr = consume(Terminals.MULTOPR);
            IFactor nts_factor = factor();
            ITerm3NTS nts_term3NTS = term3NTS();
            return new Term3NTS(ts_multOpr, nts_factor, nts_term3NTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR || currentTerminal == Terminals.ADDOPR) {
            System.out.println("term3NTS ::= ε");
            return new IEpsilon.Term3NTS();
        } else {
            throw new GrammarError(Terminals.TERM3NTS, currentTerminal);
        }
    }

    // factor ::= LITERAL
    // factor ::= IDENT factorNTS
    // factor ::= castOpr factor
    // factor ::= monadicOpr factor
    // factor ::= LPAREN expr RPAREN
    private IFactor factor() throws GrammarError {
        if (currentTerminal == Terminals.LITERAL) {
            System.out.println("factor ::= LITERAL");
            IToken ts_literal = consume(Terminals.LITERAL);
            return new FactorLiteral(ts_literal);
        } else if (currentTerminal == Terminals.IDENT) {
            System.out.println("factor ::= IDENT <factorNTS>");
            IToken ts_ident = consume(Terminals.IDENT);
            IFactorNTS nts_factorNTS = factorNTS();
            return new FactorIdent(ts_ident, nts_factorNTS);
        } else if (currentTerminal == Terminals.LBRACKET) {
            System.out.println("factor ::= <castOpr> <factor>");
            ICastOpr nts_castOpr = castOpr();
            IFactor nts_factor = factor();
            return new FactorCastOpr(nts_castOpr, nts_factor);
        } else if (currentTerminal == Terminals.ADDOPR || currentTerminal == Terminals.NOTOPR) {
            System.out.println("factor ::= <monadicOpr> <factor>");
            IMonadicOpr nts_monadicOpr = monadicOpr();
            IFactor nts_factor = factor();
            return new FactorMonadicOpr(nts_monadicOpr, nts_factor);
        } else if (currentTerminal == Terminals.LPAREN) {
            System.out.println("factor ::= LPAREN <expr> RPAREN");
            IToken ts_lParen = consume(Terminals.LPAREN);
            IExpr nts_expr = expr();
            IToken ts_rParen = consume(Terminals.RPAREN);
            return new FactorLParen(ts_lParen, nts_expr, ts_rParen);
        } else {
            throw new GrammarError(Terminals.FACTOR, currentTerminal);
        }
    }

    // factorNTS ::= INIT
    // factorNTS ::= exprList
    // factorNTS ::= ε
    private IFactorNTS factorNTS() throws GrammarError {
        if (currentTerminal == Terminals.INIT) {
            System.out.println("factorNTS ::= INIT");
            IToken ts_init = consume(Terminals.INIT);
            return new FactorNTSInit(ts_init);
        } else if (currentTerminal == Terminals.LPAREN) {
            System.out.println("factorNTS ::= <exprList>");
            IExprList nts_exprList = exprList();
            return new FactorNTSExprList(nts_exprList);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.MULTOPR) {
            System.out.println("factorNTS ::= ε");
            return new IEpsilon.FactorNTS();
        } else {
            throw new GrammarError(Terminals.FACTORNTS, currentTerminal);
        }
    }

    // exprList ::= LPAREN exprListLparenNTS RPAREN
    private IExprList exprList() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN) {
            System.out.println("exprList ::= LPAREN <exprListLparenNTS> RPAREN");
            IToken ts_lParen = consume(Terminals.LPAREN);
            IExprListLparenNTS nts_exprListLparenNTS = exprListLparenNTS();
            IToken ts_rParen = consume(Terminals.RPAREN);
            return new ExprList(ts_lParen, nts_exprListLparenNTS, ts_rParen);
        } else {
            throw new GrammarError(Terminals.EXPRLIST, currentTerminal);
        }
    }

    // exprListLparenNTS ::= expr exprListNTS
    // exprListLparenNTS ::= ε
    private IExprListLparenNTS exprListLparenNTS() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL || currentTerminal == Terminals.LBRACKET) {
            System.out.println("exprListLparenNTS ::= <expr> <exprListNTS>");
            IExpr nts_expr = expr();
            IExprListNTS nts_exprListNTS = exprListNTS();
            return new ExprListLParenNTS(nts_expr, nts_exprListNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("exprListLparenNTS ::= ε");
            return new IEpsilon.ExprListLParenNTS();
        } else {
            throw new GrammarError(Terminals.EXPRLISTLPARENNTS, currentTerminal);
        }
    }

    // exprListNTS ::= COMMA expr exprListNTS
    // exprListNTS ::= ε
    private IExprListNTS exprListNTS() throws GrammarError {
        if (currentTerminal == Terminals.COMMA) {
            System.out.println("exprListNTS ::= COMMA <expr> <exprListNTS>");
            IToken ts_comma = consume(Terminals.COMMA);
            IExpr nts_expr = expr();
            IExprListNTS nts_exprListNTS = exprListNTS();
            return new ExprListNTS(ts_comma, nts_expr, nts_exprListNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("exprListNTS ::= ε");
            return new IEpsilon.ExprListNTS();
        } else {
            throw new GrammarError(Terminals.EXPRLISTNTS, currentTerminal);
        }
    }

    // monadicOpr ::= NOT
    // monadicOpr ::= ADDOPR
    private IMonadicOpr monadicOpr() throws GrammarError {
        if (currentTerminal == Terminals.NOTOPR) {
            System.out.println("monadicOpr ::= NOT");
            IToken ts_not = consume(Terminals.NOTOPR);
            return new MonadicOprNot(ts_not);
        } else if (currentTerminal == Terminals.ADDOPR) {
            System.out.println("monadicOpr ::= ADDOPR");
            IToken ts_addOpr = consume(Terminals.ADDOPR);
            return new MonadicOprAddOpr(ts_addOpr);
        } else {
            throw new GrammarError(Terminals.MONADICOPR, currentTerminal);
        }
    }

    // castOpr ::= LBRACKET TYPE RBRACKET
    private ICastOpr castOpr() throws GrammarError {
        if (currentTerminal == Terminals.LBRACKET) {
            System.out.println("castOpr ::= LBRACKET TYPE RPAREN");
            IToken ts_lBracket = consume(Terminals.LBRACKET);
            IToken ts_type = consume(Terminals.TYPE);
            IToken ts_rBracket = consume(Terminals.RBRACKET);
            return new CastOpr(ts_lBracket, ts_type, ts_rBracket);
        } else {
            throw new GrammarError(Terminals.CASTOPR, currentTerminal);
        }
    }

    // cpsStoDecl ::= stoDecl cpsStoDeclNTS
    private ICpsStoDecl cpsStoDecl() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("cpsStoDecl ::= <stoDecl> <cpsStoDeclNTS>");
            IStoDecl nts_stoDecl = stoDecl();
            ICpsStoDeclNTS nts_cpsStoDeclNTS = cpsStoDeclNTS();
            return new CpsStoDecl(nts_stoDecl, nts_cpsStoDeclNTS);
        } else {
            throw new GrammarError(Terminals.CPSSTODECL, currentTerminal);
        }
    }

    // cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
    // cpsStoDeclNTS ::= ε
    private ICpsStoDeclNTS cpsStoDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsStoDeclNTS ::= SEMICOLON <stoDecl> <cpsStoDeclNTS>");
            IToken ts_semicolon = consume(Terminals.SEMICOLON);
            IStoDecl nts_stoDecl = stoDecl();
            ICpsStoDeclNTS nts_cpsStoDeclNTS = cpsStoDeclNTS();
            return new CpsStoDeclNTS(ts_semicolon, nts_stoDecl, nts_cpsStoDeclNTS);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("cpsStoDeclNTS ::= ε");
            return new IEpsilon.CpsStoDeclNTS();
        } else {
            throw new GrammarError(Terminals.CPSSTODECLNTS, currentTerminal);
        }
    }

    // procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
    private IProcDecl procDecl() throws GrammarError {
        if (currentTerminal == Terminals.PROC) {
            System.out.println("procDecl ::= PROC IDENT <paramList> <procDeclNTS> DO <cpsCmd> ENDPROC");
            IToken ts_proc = consume(Terminals.PROC);
            IToken ts_ident = consume(Terminals.IDENT);
            IParamList nts_paramList = paramList();
            IProcDeclNTS nts_procDeclNTS = procDeclNTS();
            IToken ts_do = consume(Terminals.DO);
            ICpsCmd nts_cpsCmd = cpsCmd();
            IToken ts_endProc = consume(Terminals.ENDPROC);
            return new ProcDecl(ts_proc, ts_ident, nts_paramList, nts_procDeclNTS, ts_do, nts_cpsCmd, ts_endProc);
        } else {
            throw new GrammarError(Terminals.PROCDECL, currentTerminal);
        }
    }

    // procDeclNTS ::= LOCAL cpsStoDecl
    // procDeclNTS ::= ε
    private IProcDeclNTS procDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.LOCAL) {
            System.out.println("procDeclNTS ::= LOCAL <cpsStoDecl>");
            IToken ts_local = consume(Terminals.LOCAL);
            ICpsStoDecl nts_cpsStoDecl = cpsStoDecl();
            return new ProcDeclNTS(ts_local, nts_cpsStoDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("procDeclNTS ::= ε");
            return new IEpsilon.ProcDeclNTS();
        } else {
            throw new GrammarError(Terminals.PROCDECLNTS, currentTerminal);
        }
    }
}
