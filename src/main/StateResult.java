package main;

/**
 * Interface that represents the action taken after handling a user
 * input. Because this is a sealed interface, it allows you to pattern
 * match the record classes inside of a switch-statement (Java 21+?).
 * 
 * You should staticically import this using a wildcard ({@code .*})
 * <pre>
 * import static main.State.*; // import everything statically
 * StateResult sr = ...
 * switch (sr) {
 *     case Transition(State next) -> next;
 *     case Stay() -> this;
 *     case Exit() -> {
 *         ...
 *     }
 *     case Error(String message) -> {
 *         ...
 *     }
 * }
 * </pre>
 */
public sealed interface StateResult
        permits StateResult.Transition,
                StateResult.Stay,
                StateResult.Exit,
                StateResult.Fail
{
    public static record Transition(State next) implements StateResult {}
    public static record Stay()                 implements StateResult {}
    public static record Exit()                 implements StateResult {}
    public static record Fail(String message)   implements StateResult {}

    public static Transition transition(State next) {
        return new Transition(next);
    }
    
    public static Stay stay() {
        return new Stay();
    }

    public static Exit exit() {
        return new Exit();
    }
    
    public static Fail fail(String message) {
        return new Fail(message);
    }
}

