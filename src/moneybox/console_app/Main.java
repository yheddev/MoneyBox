package moneybox.console_app;

import moneybox.basic_logic.Goal;
import moneybox.basic_logic.GoalStorage;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        GoalStorage storage = new GoalStorage();
        ConsoleApplication application = new ConsoleApplication(storage);
        storage.addGoal(new Goal("Ноутбук", 1000));
        storage.addGoal(new Goal("Велосипед", 500));
        application.run();


    }
}