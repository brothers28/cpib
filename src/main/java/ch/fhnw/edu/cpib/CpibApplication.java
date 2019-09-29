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

    public static void main(String[] args) {
        // Start spring application
        SpringApplication.run(CpibApplication.class, args);

        ImlReader reader = new ImlReader();
        CharSequence iml = reader.readFile(FILE_NAME);

        Scanner scanner = new Scanner();
        TokenList tokens = scanner.scan(iml);
    }

}
