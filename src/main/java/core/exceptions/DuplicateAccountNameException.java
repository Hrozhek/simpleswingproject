package core.exceptions;

public class DuplicateAccountNameException extends RuntimeException {
    private static final String MESSAGE = "Account with name %s already exist, please. choose another name";
    public DuplicateAccountNameException(String name) {
        super(name);
    }
}
