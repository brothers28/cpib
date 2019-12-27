package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<IToken> tokenList = new ArrayList<>();
    private int position = 0;

    public void add(IToken token) {
        tokenList.add(token);
    }

    public void reset() {
        position = 0;
    }

    public IToken nextToken() {
        return tokenList.get(position++);
    }

    public IToken get(int index) {
        position = index + 1;
        return tokenList.get(index);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (IToken t : tokenList) {
            sb.append(t.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        return sb.toString();
    }
}
