package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.AddOpr;
import ch.fhnw.edu.cpib.scanner.symbols.BoolOpr;
import ch.fhnw.edu.cpib.scanner.symbols.RelOpr;

import java.util.Map;

import static java.util.Map.entry;

public class Scanner {
    public static final Map<String, Token> keywords;
    public static final Map<String, Token> symbols;

    static {
        // Build symbols map
        symbols = Map.ofEntries(
                entry("<", new RelOpr(Operators.LT)),
                entry("<=", new RelOpr(Operators.LE)),
                entry(">", new RelOpr(Operators.GT)),
                entry(">=", new RelOpr(Operators.GE)),
                entry("=", new RelOpr(Operators.EQ)),
                entry("/=", new RelOpr(Operators.NE)),
                entry("+", new AddOpr(Operators.PLUS)),
                entry("-", new AddOpr(Operators.MINUS)),
                entry("&", new BoolOpr(Operators.AND)),
                entry("|", new BoolOpr(Operators.OR)),
                entry("&&", new BoolOpr(Operators.CAND)),
                entry("||", new BoolOpr(Operators.COR)),
                entry("(", Terminals.LPAREN),
                entry(")", Terminals.RPAREN),
                entry(",", Terminals.COMMA),
                entry(";", Terminals.SEMICOLON),
                entry(":", Terminals.COLON),
                entry(":=", Terminals.BECOMES));

        // Build keywords map
        keywords = Map.ofEntries(
                entry("while", Terminals.WHILE),
                entry("endwhile", Terminals.ENDWHILE),
                entry("do", Terminals.DO),
                entry("program", Terminals.PROGRAM),
                entry("endprogram", Terminals.ENDPROGRAM),
                entry("proc", Terminals.PROC),
                entry("endproc", Terminals.ENDPROC),
                entry("call", Terminals.CALL ),
                entry("debugin", Terminals.DEBUGIN),
                entry("debugout", Terminals.DEBUGOUT),
                entry("else", Terminals.ELSE),
                entry("endfun", Terminals.ENDFUN),
                entry("endif", Terminals.ENDIF),
                entry("fun", Terminals.FUN),
                entry("global", Terminals.GLOBAL),
                entry("if", Terminals.IF),
                entry("init", Terminals.INIT),
                entry("local", Terminals.LOCAL),
                entry("notopr", Terminals.NOTOPR),
                entry("returns", Terminals.RETURNS),
                entry("skip", Terminals.SKIP),
                entry("then", Terminals.THEN));
    }

    /**
     * Converts character sequence to tokens.
     *
     * @param cs Char sequence
     * @return Token list
     */
    public TokenList scan(CharSequence cs) {
        // Assert that if last char exists, it must be a "New Line"
        assert cs.length() == 0 || cs.charAt(cs.length() - 1) == '\n';

        TokenList result = new TokenList();
        int state = 0;
        StringBuffer lexAcc = null; // for constructing the identifier
        StringBuffer syAcc = null; // for constructing the symbols
        long numAccu = 0L; // for constructing the literal value

        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);

            switch (state) {
            case 0:
                if (isSymbol(c)) {
                    state = 1;
                    syAcc = new StringBuffer();
                    syAcc.append(c);
                } else if (Character.isDigit(c)) {
                    state = 2;
                    int digit = Character.digit(c, 10);
                    numAccu = digit;
                } else if (Character.isAlphabetic(c)) {
                    state = 3;
                    lexAcc = new StringBuffer();
                    lexAcc.append(c);
                }
                break;
            case 1:
                if (isFollowingSymbol(c, syAcc)) {
                    state = 0;
                    syAcc.append(c);

                    Token token = symbols.get(syAcc.toString());
                    result.add(token);
                }
                else {
                    state = 0;
                    i = i - 1; // one back for next lexeme

                    Token token = symbols.get(syAcc.toString());
                    result.add(token);
                }
                break;
            case 2:
                if (Character.isDigit(c)) {
                    state = 2;
                    int digit = Character.digit(c, 10);
                    numAccu = numAccu * 10 + digit;
                    if (numAccu > Integer.MAX_VALUE) {
                        //throw new LexicalError("Integer literal too large!");
                        // TODO ausprogrammieren
                    }
                }
                else {
                    state = 0;
                    i = i - 1; // one back for next lexeme
                    result.add(new Literal(Terminals.LITERAL, (int)numAccu));
                }
                break;
            case 3:
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    state = 3;
                    lexAcc.append(c);
                }
                else {
                    state = 0;
                    i = i - 1; // one back for next lexeme
                    if(isKeyword(lexAcc)){
                        Token token = keywords.get(lexAcc.toString());
                        result.add(token);
                    } else {
                        result.add(new Ident(Terminals.LITERAL, lexAcc.toString()));
                    }
                }
                break;
            default:
                throw new InternalError("Default case in scanner.");
            }
        }
        return result;
    }

    /**
     * Determines if character is subsequent part of symbol.
     *
     * @param c Character to check
     * @return True, if character is subsequent symbol
     */
    private boolean isFollowingSymbol(char c, StringBuffer previous) {
        switch (previous.toString())
        {
        case "/" :
            return c == '=';
        case "&" :
            return c == '&';
        case "|" :
            return c == '|';
        case ">" :
            return c == '=';
        case "<" :
            return c == '=';
        case ":" :
            return c == '=';
        default:
            return false;
        } // switch
    }

    /**
     * Checks if character is regular symbol of iml.
     *
     * @param c Current character
     * @return True if regular iml symbol
     */
    private boolean isSymbol(char c) {
        return symbols.containsKey(String.valueOf(c));
    }

    /**
     * Checks if character is keyword of iml.
     *
     * @param key Word to test
     * @return True if keyword
     */
    private boolean isKeyword(StringBuffer key) {
        return keywords.containsKey(key.toString());
    }
}
