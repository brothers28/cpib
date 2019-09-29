package ch.fhnw.edu.cpib;

import ch.fhnw.edu.cpib.scanner.RelOpr;
import ch.fhnw.edu.cpib.scanner.Scanner;
import ch.fhnw.edu.cpib.scanner.TokenList;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.util.ImlReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

import static java.util.Map.entry;

@SpringBootApplication public class CpibApplication {
    public static final String FILE_NAME = "/example.iml";

    public static final Map<String, Object> keywords;
    public static final Map<String, Object> symbols;

    static {
        // Build symbols map
        symbols = Map.ofEntries(entry("<", new RelOpr(Terminals.RELOPR, Operators.LT)),
                entry(">=", new RelOpr(Terminals.RELOPR, Operators.LE)), entry(":", Terminals.COLON),
                entry(":=", Terminals.BECOMES));

        // Build keywords map
        keywords = Map.ofEntries(entry("while", Terminals.WHILE), entry("endwhile", Terminals.ENDWHILE),
                entry("do", Terminals.DO));
    }

    public static void main(String[] args) {
        // Start spring application
        SpringApplication.run(CpibApplication.class, args);

        ImlReader reader = new ImlReader();
        CharSequence iml = reader.readFile(FILE_NAME);

        Scanner scanner = new Scanner();
        TokenList tokens = scanner.scan(iml);
    }

}
