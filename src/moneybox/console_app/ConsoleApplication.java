package moneybox.console_app;

import moneybox.basic_logic.Goal;
import moneybox.basic_logic.GoalNotFoundException;
import moneybox.basic_logic.GoalStorage;

import java.util.Map;
import java.util.Scanner;

public class ConsoleApplication {
    private final GoalStorage storage;

    public ConsoleApplication(GoalStorage storage) {
        this.storage = storage;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a command. Type exit to quit.");

        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")){
                System.out.println("Program terminated.");
                return;
            }

            handleCommand(line);
        }
    }

    private void handleCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        String command = parts[0];

        if (command.equalsIgnoreCase("goals")) {
            printGoals();
        } else if (command.equalsIgnoreCase("create")) {
            if (parts.length < 2) {
                System.out.println("Format: create <name> <amount>");
                return;
            }
            handleCreate(parts[1]);
        } else if (command.equalsIgnoreCase("deposit")){
            if (parts.length < 2){
                System.out.println("Format: deposit <id> <amount>");
                return;
            }

            handleDeposit(parts[1]);
        } else if (command.equalsIgnoreCase("withdraw")){
            if (parts.length < 2){
                System.out.println("Format: withdraw <id> <amount>");
                return;
            }

            handleWithdraw(parts[1]);
        } else if (command.equalsIgnoreCase("target")) {
            if (parts.length < 2){
                System.out.println("Format: target <id> <amount>");
                return;
            }

            handleTarget(parts[1]);
        } else if (command.equalsIgnoreCase("rename")) {
            if (parts.length < 2){
                System.out.println("Format: rename <id> <name>");
                return;
            }

            handleRename(parts[1]);
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    private void printGoals() {
        Map<Long, Goal> goals = storage.getGoals();

        if (goals.isEmpty()) {
            System.out.println("There are no goals yet.");
            return;
        }

        for (Goal goal : goals.values()) {
            System.out.println(
                    goal.getId() + ": " + goal.getName()
                            + " — " + goal.getBalance()
                            + "/" + goal.getTargetAmount()
                            + ", reached: " + goal.isReached()
            );
        }
    }

    private void handleCreate(String arguments) {
        int separator = arguments.lastIndexOf(" ");

        if (separator == -1) {
            System.out.println("Format: create <name> <amount>");
            return;
        }

        String name = arguments.substring(0, separator).trim();
        String amountText = arguments.substring(separator + 1);

        try {
            long targetAmount = Long.parseLong(amountText);
            Goal goal = new Goal(name, targetAmount);
            storage.addGoal(goal);

            System.out.println("Goal created. ID: " + goal.getId());
        } catch (NumberFormatException e) {
            System.out.println("Amount must be an integer, you entered: " + amountText);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create goal: " + e.getMessage());
        }
    }

    private void handleDeposit(String arguments) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            System.out.println("Format: deposit <id> <amount>");
            return;
        }

        try {
            long id = Long.parseLong(values[0]);
            long amount = Long.parseLong(values[1]);

            storage.findGoal(id).deposit(amount);
            System.out.println("Goal ID: " + id);
            System.out.println("Deposit amount: " + amount);
        } catch (NumberFormatException e) {
            System.out.println("ID and amount must be integers.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to deposit money: " + e.getMessage());
        }
    }

    private void handleWithdraw(String arguments) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            System.out.println("Format: withdraw <id> <amount>");
            return;
        }

        try {
            long id = Long.parseLong(values[0]);
            long amount = Long.parseLong(values[1]);

            storage.findGoal(id).withdraw(amount);
            System.out.println("Goal ID: " + id);
            System.out.println("Withdrawal amount: " + amount);
        } catch (NumberFormatException e) {
            System.out.println("ID and amount must be integers.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to withdraw money: " + e.getMessage());
        }
    }

    private void handleTarget(String arguments) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            System.out.println("Format: target <id> <amount>");
            return;
        }

        try{
            long id = Long.parseLong(values[0]);
            long amount = Long.parseLong(values[1]);
            storage.findGoal(id).changeTargetAmount(amount);
            System.out.println("Target amount changed to " + amount);
        } catch (NumberFormatException e) {
            System.out.println("ID and amount must be integers.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to change target amount: " +
                    e.getMessage());
        }
    }

    private void handleRename(String arguments){
        String[] values = arguments.trim().split("\\s+", 2);

        if (values.length != 2) {
            System.out.println("Format: rename <id> <name>");
            return;
        }

        try {
            long id = Long.parseLong(values[0]);
            String name = values[1];
            storage.findGoal(id).changeGoalName(name);
            System.out.println("Goal " + id + " renamed to \"" + name + "\".");
        } catch (NumberFormatException e) {
            System.out.println("ID must be integer.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to rename the goal: " +
                    e.getMessage());
        }

    }

}