package main;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import main.StateResult.Exit;
import main.StateResult.Fail;
import main.StateResult.Stay;
import main.StateResult.Transition;
import sql.DbConnection;

/** Simple UI stdin and stdout. */
public final class Dialog implements AutoCloseable
{
    /** Internal Scanner used to retrieve user input from the stdin. */
    private final Scanner scanner;

    /** Constructs a new Dialog using a default Scanner. */
    public Dialog() {
        scanner = new Scanner(System.in);
    }
    
    /**
     * Constructs a new Dialog with the internal Scanner to be used.
     * 
     * @param scanner the scanner to be used
     */
    public Dialog(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner);
    }

    /**
     * Opens the input loop, beginning at a "welcome screen" determined
     * by the database connection.
     * 
     * @param con the database to be connected to
     */
    public void open(DbConnection con) {
        State currentState = Welcome.toDatabase(con);
        for (;;)
        {
            System.out.println(currentState.prompt());

            var input = scanner.nextLine();
            var result = currentState.handle(input, con);

            // Java 21+ pattern matching
            currentState = switch (result)
            {
                case Transition(State state) -> state;
                case Stay()                  -> currentState;

                case Fail(String message) -> {
                    System.err.println(message);
                    yield currentState;
                }
                case Exit() -> {
                    close();
                    System.exit(1);
                    yield null;
                }
            };
        }
    }

    /**
     * Closes the dialog and underlying scanner.
     */
    @Override
    public void close() {
        System.out.println("[   ] Closing...");
        scanner.close();
        System.out.println("[ok.]");
    }
}