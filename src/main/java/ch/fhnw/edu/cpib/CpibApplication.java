package ch.fhnw.edu.cpib;

import ch.fhnw.edu.cpib.ast.AstTree;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.parser.Parser;
import ch.fhnw.edu.cpib.scanner.Scanner;
import ch.fhnw.edu.cpib.scanner.TokenList;
import ch.fhnw.edu.cpib.scanner.util.ImlReader;
import ch.fhnw.edu.cpib.vm.ICodeArray;
import ch.fhnw.edu.cpib.vm.IVirtualMachine;
import ch.fhnw.edu.cpib.vm.VirtualMachine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication public class CpibApplication {
    public static final String FILE_NAME = "/example_init";

    public static void main(String[] args) {
        // Start spring application
        SpringApplication.run(CpibApplication.class, args);

        ImlReader reader = new ImlReader();
        CharSequence iml = reader.readFile(FILE_NAME);

        Scanner scanner = new Scanner();
        TokenList tokens = null;

        try {
            tokens = scanner.scan(iml);
            System.out.println("---------------------------------------------");
            System.out.println("Char List:");
            System.out.println(iml.toString());
            System.out.println("---------------------------------------------");
            System.out.println("TokenList:");
            System.out.println(tokens.toString());
            System.out.println("---------------------------------------------");
        } catch (LexicalError e) {
            e.printStackTrace();
        }

        Parser parser = new Parser(tokens);
        AstTree ast = null;

        try {
            ast = parser.parse();
        } catch (GrammarError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (AlreadyDeclaredError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (NotDeclaredError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (AlreadyGloballyDeclaredError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (LRValueError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (InvalidParamCountError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (AlreadyInitializedError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (TypeCheckError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (NotInitializedError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (GlobalProtectedInitializationError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (CannotAssignToConstError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        } catch (CastError e) {
            System.out.println(" Error...\n");
            e.printStackTrace();
        }

        IVirtualMachine virtualMachine = null;
        try {
            System.out.println("\n---------------------------------------------------\n");
            System.out.println("Generating code array :\n");
            ICodeArray codeArray = ast.getCodeArray();
            System.out.println(codeArray.toString());

            virtualMachine = new VirtualMachine(codeArray, 65536);
        } catch (IVirtualMachine.ExecutionError e) {
            System.out.println("\nVM error...\n");
            e.printStackTrace();
        } catch (ICodeArray.CodeTooSmallError e) {
            System.out.println("\nVM error...\n");
            e.printStackTrace();
        }
    }

}
