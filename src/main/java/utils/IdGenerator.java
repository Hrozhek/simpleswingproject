package utils;

public final class IdGenerator {
    private static long id = 1L;

    private IdGenerator() {}

    public static long generateId() {
        return id++;
    }
}
