package dto;

/**
 * A sealed interface that marks a data transfer object (DTO).
 * (see package-info.java)
 */
public sealed interface DbObject permits Machine, Inventory
{
    /**
     * Returns a user-friendly string to be displayed in the REPL.
     * @return the string to be displayed
     */
    public String toDisplayString();
}
