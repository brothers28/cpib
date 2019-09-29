package ch.fhnw.edu.cpib.scanner;

public class Scanner {
    public static TokenList scan(CharSequence cs){
        // Assert that if last char exists, it must be a "New Line"
        assert cs.length() == 0 || cs.charAt(cs.length() - 1) == '\n';

        TokenList result = new TokenList();
        int state = 0;
        StringBuffer lexAcc = null; // for constructing the identifier
        long numAccu = 0L; // for constructing the literal value

        for (int i = 0; i < cs.length(); i++){

        }



        return result;
    }
}
