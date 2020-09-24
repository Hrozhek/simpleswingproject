package core.exceptions;

public class ForeignKeyConstraintException extends RuntimeException {
    private static final String MESSAGE = "FK constraints violated: no entity with id %d found";
    public ForeignKeyConstraintException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
