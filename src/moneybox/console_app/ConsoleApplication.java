package moneybox.console_app;

import moneybox.basic_logic.Goal;
import moneybox.basic_logic.GoalNotFoundException;
import moneybox.basic_logic.GoalStorage;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleApplication {
    private final GoalStorage storage;

    private record IdAndAmount(long id, long amount) {}

    public ConsoleApplication(GoalStorage storage) {
        this.storage = Objects.requireNonNull(storage, "Storage cannot be null.");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type help to see available commands. Type exit to quit.");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Program terminated.");
                return;
            }

            try {
                handleCommand(line);
            } catch (NumberFormatException e) {
                System.out.println("Numeric arguments must be integers within the long range.");
            } catch (GoalNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Command failed: " + e.getMessage());
            }
        }
    }

    private void handleCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        String command = parts[0];
        String arguments = parts.length == 2 ? parts[1] : "";

        if (command.equalsIgnoreCase("help")) {
            requireNoArguments(arguments, "help");
            printHelp();
        } else if (command.equalsIgnoreCase("goals")) {
            requireNoArguments(arguments, "goals");
            printGoals();
        } else if (command.equalsIgnoreCase("create")) {
            handleCreate(arguments);
        } else if (command.equalsIgnoreCase("deposit")) {
            handleDeposit(arguments);
        } else if (command.equalsIgnoreCase("withdraw")) {
            handleWithdraw(arguments);
        } else if (command.equalsIgnoreCase("target")) {
            handleTarget(arguments);
        } else if (command.equalsIgnoreCase("rename")) {
            handleRename(arguments);
        } else if (command.equalsIgnoreCase("exit")) {
            throw new IllegalArgumentException("Format: exit");
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    private void requireNoArguments(String arguments, String command) {
        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException("Format: " + command);
        }
    }

    private void printHelp() {
        System.out.println("""
                help                      Show available commands
                goals                     Show all goals
                create <name> <amount>    Create a goal
                deposit <id> <amount>     Deposit money
                withdraw <id> <amount>    Withdraw money
                target <id> <amount>      Change target amount
                rename <id> <name>        Rename a goal
                exit                      Quit
                """);
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

    private IdAndAmount parseIdAndAmount(String arguments, String command) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            throw new IllegalArgumentException(
                    "Format: " + command + " <id> <amount>"
            );
        }

        long id = Long.parseLong(values[0]);
        long amount = Long.parseLong(values[1]);

        return new IdAndAmount(id, amount);
    }

    private void handleCreate(String arguments) {
        arguments = arguments.trim();
        int separator = arguments.lastIndexOf(' ');

        if (separator == -1) {
            throw new IllegalArgumentException("Format: create <name> <amount>");
        }

        String name = arguments.substring(0, separator).trim();
        String amountText = arguments.substring(separator + 1);
        long targetAmount = Long.parseLong(amountText);

        Goal goal = new Goal(name, targetAmount);
        storage.addGoal(goal);

        System.out.println("Goal created. ID: " + goal.getId());
    }

    private void handleDeposit(String arguments) {
        IdAndAmount values = parseIdAndAmount(arguments, "deposit");
        Goal goal = storage.findGoal(values.id());

        goal.deposit(values.amount());

        System.out.println(
                "Deposited " + values.amount()
                        + " to goal " + goal.getId()
                        + ". Balance: " + goal.getBalance()
        );
    }

    private void handleWithdraw(String arguments) {
        IdAndAmount values = parseIdAndAmount(arguments, "withdraw");
        Goal goal = storage.findGoal(values.id());

        goal.withdraw(values.amount());

        System.out.println(
                "Withdrew " + values.amount()
                        + " from goal " + goal.getId()
                        + ". Balance: " + goal.getBalance()
        );
    }

    private void handleTarget(String arguments) {
        IdAndAmount values = parseIdAndAmount(arguments, "target");
        Goal goal = storage.findGoal(values.id());

        goal.changeTargetAmount(values.amount());

        System.out.println(
                "Target amount for goal " + goal.getId()
                        + " changed to " + goal.getTargetAmount()
        );
    }

    private void handleRename(String arguments) {
        String[] values = arguments.trim().split("\\s+", 2);

        if (values.length != 2) {
            throw new IllegalArgumentException("Format: rename <id> <name>");
        }

        long id = Long.parseLong(values[0]);
        String name = values[1];
        Goal goal = storage.findGoal(id);

        goal.changeGoalName(name);

        System.out.println(
                "Goal " + goal.getId() + " renamed to \"" + goal.getName() + "\"."
        );
    }
}