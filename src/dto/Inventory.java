package dto;

public record Inventory(
        int itemId,
        String name,
        String description,
        int amount,
        int price,
        int slot) implements DbObject
{
    @Override
    public String toDisplayString() {
        return "Slot %02d: | %6s | %02d STK | %15s | %s"
                .formatted(slot(), getPrice(), amount(),
                        name(), description());
    }
    
    public String getPrice() {
        return "%d,%02d€".formatted(price() / 100, price() % 100);
    }
}
