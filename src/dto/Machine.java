package dto;

public record Machine(
        int id,
        int modelId,
        String modelName,
        int staffId,
        int locationId,
        String street,
        String city,
        String country,
        int balance,
        boolean isRunning) implements DbObject
{ 
    @Override
    public String toDisplayString() {
        return "| id: %5s | [%s] | %10s | %20s | %10s, %s"
                .formatted(id(), isRunning() ? " ON" : "OFF",
                        modelName(), street(), city(), country());
    }
}
