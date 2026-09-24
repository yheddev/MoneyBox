package moneybox.basic_logic;

public class MoneyBox {
    private long balance = 0;

    public long getBalance() {
        return balance;
    }

    public MoneyBox() {}

    public void deposit(long amount){
        AmountValidator.requirePositive(amount);

        if (amount > Long.MAX_VALUE - balance) {
            throw new IllegalArgumentException("Deposit would overflow the balance.");
        }

        balance += amount;
    }

    public void withdraw(long amount){
        AmountValidator.requirePositive(amount);
        if (amount > balance){
            throw new IllegalArgumentException(
                    "The withdrawal amount cannot exceed the balance."
            );
        }
        
        balance -= amount;
    }
}
