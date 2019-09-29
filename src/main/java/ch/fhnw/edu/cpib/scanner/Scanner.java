package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import com.sun.jdi.Value;

import java.util.Map;

import static java.util.Map.entry;

public class Scanner {
    public static final Map<String, Token> keywords;
    public static final Map<String, Token> symbols;

    static {
        // Build symbols map
        symbols = Map.ofEntries(
                entry("<", new RelOpr(Operators.LT)),
                entry(">=", new RelOpr(Operators.LE)),
                entry(":", Terminals.COLON),
                entry(":=", Terminals.BECOMES));

        // Build keywords map
        keywords = Map.ofEntries(
                entry("while", Terminals.WHILE),
                entry("endwhile", Terminals.ENDWHILE),
                entry("do", Terminals.DO));
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
     * Checks if character is regular symbol of iml.
     *
     * @param c Current character
     * @return True if regular iml symbol
     */
    private boolean isSymbol(char c) {
        return symbols.containsKey(c);
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
