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

    /**
     * Move to next state.
     * 
     * @param next the state to be moved into
     * @return
     */
    public static Transition transition(State next) {
        return new Transition(next);
    }
    
    /**
     * Stay in the current state.
     * 
     * @return
     */
    public static Stay stay() {
        return new Stay();
    }

    /**
     * Exit the application.
     * 
     * @return
     */
    public static Exit exit() {
        return new Exit();
    }
    
    /**
     * Stay in the current state, displaying an error message.
     * 
     * @param message
     * @return
     */
    public static Fail fail(String message) {
        return new Fail(message);
    }
}

