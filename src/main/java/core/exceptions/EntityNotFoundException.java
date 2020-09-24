package core.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Entity with %s not found";

    public EntityNotFoundException(long id) {
        super(String.format(MESSAGE, "id " + id));
    }

    public EntityNotFoundException(String name) {
        super(String.format(MESSAGE, "name " + name));
    }
}
