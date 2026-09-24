package moneybox;

public class GoalNotFoundException extends RuntimeException {

    public GoalNotFoundException(long id) {
        super("Goal with id " + id + " was not found.");
    }
}