package ch.fhnw.edu.cpib.scanner.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImlReader {

    /**
     * Reads iml file.
     *
     * @param fileName Name of iml file
     * @return Char sequence of iml
     */
    public CharSequence readFile(String fileName) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(getPath(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * Gets path from resources folder.
     *
     * @param fileName Filename
     * @return Path to file
     */
    private Path getPath(String fileName) {
        try {
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
