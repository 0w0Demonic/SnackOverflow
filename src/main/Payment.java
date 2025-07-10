package main;

import static main.StateResult.*;

import java.util.Objects;

import dto.Inventory;
import dto.Machine;
import sql.DbConnection;

/**
 * Payment screen.
 */
public final class Payment implements State
{
    private final Inventory inv;
    private final Machine m;
    private final DbConnection db;

    public Payment(Inventory inv, Machine m, DbConnection db) {
        this.inv = Objects.requireNonNull(inv);
        this.m = Objects.requireNonNull(m);
        this.db = Objects.requireNonNull(db);
    }
    
    private boolean firstTime = true;

    @Override
    public String prompt() {
        if (!firstTime) {
            return "";
        }
        firstTime = false;

        return """
        Payment for:
        - %s
        - %s
        
        Please insert cash or select payment type:
        0 - credit card
        1 - firstborn child
        2 - good ol' cash (not recommended)
        """.formatted(inv.name(), inv.getPrice());
    }

    @Override
    public StateResult handle(String input, DbConnection con) {
        return switch (input) {
            case "0" -> {
                try {
                    System.out.println("Insert card...");
                    Thread.sleep(1000);
                    System.out.println("Paying...");
                    Thread.sleep(2500);
                    System.out.println("Thank you!");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                db.buy(m.id(), inv.itemId());
                yield transition(new VendingMachine(m, db));
            }
            case "1" -> fail("Don't do that!");
            case "2" -> {
                try {
                    System.out.println("Preparing for payment...");
                    Thread.sleep(4000);
                    System.out.println("Wait... what's this?");
                    Thread.sleep(1000);
                    System.out.println("I'm 3reak1ng ap rt. .;-\\ `");
                    Thread.sleep(2000);
                    System.out.println("H   e  l   .    -   p \\.");
                    Thread.sleep(3000);
                    System.out.println("\r\n\r\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                db.switchOff(m.id());
                yield transition(new Welcome(db));
            }
            default -> fail("What?");
        };
    }
}
