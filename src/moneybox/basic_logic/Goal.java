package moneybox.basic_logic;

public class Goal {
    private final long id;
    private static long nextId = 1;
    private String name;
    private long targetAmount;
    private final MoneyBox moneyBox;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTargetAmount() {
        return targetAmount;
    }

    public Goal(String name, long targetAmount){
        validateName(name);
        this.name = name;
        AmountValidator.requirePositive(targetAmount);
        this.targetAmount = targetAmount;
        id = nextId;
        nextId++;
        this.moneyBox = new MoneyBox();
    }

    public void changeTargetAmount(long newAmount){
        AmountValidator.requirePositive(newAmount);
        targetAmount = newAmount;
    }

    public void changeGoalName(String newName){
        validateName(newName);
        name = newName;
    }

    private void validateName(String name){
        if (name == null){
            throw new IllegalArgumentException("" +
                    "The Name cannot be null."
            );
        }

        if (name.isBlank()){
            throw new IllegalArgumentException(
                    "The name cannot consist of spaces."
            );
        }
    }

    public boolean isReached(){
        return moneyBox.getBalance() >= targetAmount;
    }

    public void deposit(long amount){
        moneyBox.deposit(amount);
    }

    public void withdraw(long amount){
        moneyBox.withdraw(amount);
    }

    public long getBalance(){
        return moneyBox.getBalance();
    }
}
