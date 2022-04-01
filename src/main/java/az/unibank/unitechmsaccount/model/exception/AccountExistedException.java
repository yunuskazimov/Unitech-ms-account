package az.unibank.unitechmsaccount.model.exception;

public class AccountExistedException extends RuntimeException {
    public AccountExistedException(String message) {
        super(message);
    }
}
