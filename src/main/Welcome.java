package main;

import static java.util.stream.Collectors.joining;

import static main.StateResult.*;

import java.util.Objects;
import java.util.Optional;

import dto.DbObject;
import dto.Machine;
import sql.DbConnection;

/**
 * Represents the welcome screen in which the users chooses the
 * vending machine to be used.
 */
public final class Welcome implements State
{
    /**
     * Connection to database.
     */
    private final DbConnection db;

    /**
     * Constructs a new instance
     * @param db the database connection to be used
     */
    Welcome(DbConnection db) {
        this.db = Objects.requireNonNull(db);
    }
    
    /**
     * Constructs a new instance by connecting to the given database.
     * 
     * @param db the database connection to be used
     * @return a new welcome screen
     */
    public static Welcome toDatabase(DbConnection db) {
        return new Welcome(db);
    }

    /**
     * ASCII art and formatting.
     */
    private static final String PROMPT = """
    ______________
      ,._                                       _.,
     (             _.  /    _._ \\ / ,._       /      _._  \\ /\\ /
      \\  /~, ,-., (_  /__  ( o ) V  L/   /_  /-  /  ( o )  v  v
    .__\\/ /  `´/     / \\    `-´     \\_, /   /   /_   `-´
    ______________                       --´                     
    ------
    Welcome!
    
    Please choose vending machine:
    ----
    %s
    """;
    
    /** Whether the message above was displayed for the first time. */
    private boolean firstTime = true;

    /**
     * Displays a text.
     */
    @Override
    public String prompt() {
        if (firstTime) {
            firstTime = false;
            var machines = db.allMachines().stream()
                             .map(DbObject::toDisplayString)
                             .collect(joining("\r\n"));
            return PROMPT.formatted(machines);
        }
        return "";
    }

    /**
     * Handles incoming user input.
     */
    @Override
    public StateResult handle(String input, DbConnection db) {
        if (!Util.isNumber(input)) {
            return fail("Error: expected a number.");
        }

        int id = Integer.parseInt(input);
        Optional<Machine> machine = db.machineById(id);
        if (machine.isEmpty()) {
            return fail("Error: vending machine doesn't exist.");
        }
        
        // little thread sleep to get vending machine started.
        if (!machine.get().isRunning()) {
            try {
                System.out.println("""
                    [STARTING VENDING MACHINE]
                    
                    This might take a while...
                    """);
                Thread.sleep(5000);
                db.switchOn(id);
                // TODO update db entry
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return transition(new VendingMachine(machine.get(), db));
    }
}