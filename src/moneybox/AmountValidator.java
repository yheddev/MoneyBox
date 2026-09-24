package moneybox;

public final class AmountValidator {

    private AmountValidator() {}

    public static void requirePositive(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }
    }
}