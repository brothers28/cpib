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
    public static final String FILE_NAME = "/example_overflowNat"; // Overflow: Execution Error
    //public static final String FILE_NAME = "/example_underflowNat"; // Underflow: Execution Error
    //public static final String FILE_NAME = "/example_casting"; // Nat -> Int, Int -> Nat, OverflowInt, UnderflowNat
    //public static final String FILE_NAME = "/example_fibo"; // fibo(8) --> 21
    //public static final String FILE_NAME = "/example_factorial"; // 4 --> 24

    public static void main(String[] args) {
        // Start spring application
        SpringApplication.run(CpibApplication.class, args);

        ImlReader reader = new ImlReader();
        CharSequence iml = reader.readFile(FILE_NAME);

        try {
            // Scan iml
            Scanner scanner = new Scanner();
            TokenList tokens = scanner.scan(iml);
            System.out.println("Char list:");
            System.out.println(iml.toString());
            System.out.println("\n---------------------------------------------------\n");
            System.out.println("Token list:");
            System.out.println(tokens.toString());
            System.out.println("\n---------------------------------------------------");

            // Parse tokens
            Parser parser = new Parser(tokens);
            AstTree ast = parser.parse();

            // Generate code array
            System.out.println("\n---------------------------------------------------\n");
            System.out.println("Generate code array :\n");
            ICodeArray codeArray = ast.getCodeArray();
            System.out.println(codeArray.toString());

            IVirtualMachine virtualMachine = new VirtualMachine(codeArray, 65536);

        } catch (LexicalError e) {
            System.out.println("\nScanner error...\n");
            e.printStackTrace();
        } catch (GrammarError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (AlreadyDeclaredError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (NotDeclaredError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (LRValError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (InvalidParamCountError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (AlreadyInitializedError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (TypeCheckError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (NotInitializedError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (AssignToConstError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (CastError e) {
            System.out.println("\nParser error...\n");
            e.printStackTrace();
        } catch (IVirtualMachine.ExecutionError e) {
            System.out.println("\nVM error...\n");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch(RuntimeException e) {
            System.out.println("\nRuntime error...\n");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ICodeArray.CodeTooSmallError e) {
            System.out.println("\nVM error...\n");
            e.printStackTrace();
        }
    }

}
