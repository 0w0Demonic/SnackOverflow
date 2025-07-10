package dto;

/**
 * Represents inventory of an item inside a vending machine.
 */
public record Inventory(
        int itemId,
        String name,
        String description,
        int amount,
        int price,
        int slot) implements DbObject
{
    /**
     * User-friendly string output.
     */
    @Override
    public String toDisplayString() {
        return "Slot %02d: | %6s | %02d STK | %15s | %s"
                .formatted(slot(), getPrice(), amount(),
                        name(), description());
    }
    
    /**
     * Currency converter.
     * @return currency converted string
     */
    public String getPrice() {
        return "%d,%02d€".formatted(price() / 100, price() % 100);
    }
}
