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

        System.out.println("Введіть команду. Для виходу-exit.");

        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")){
                System.out.println("Програма завершена.");
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
                System.out.println("Формат: create <назва> <сума>");
                return;
            }
            handleCreate(parts[1]);
        } else if (command.equalsIgnoreCase("deposit")){
            if (parts.length < 2){
                System.out.println("Формат: deposit <id> <сума>");
                return;
            }

            handleDeposit(parts[1]);
        } else if (command.equalsIgnoreCase("withdraw")){
            if (parts.length < 2){
                System.out.println("Формат: withdraw <id> <сума>");
                return;
            }

            handleWithdraw(parts[1]);
        } else {
            System.out.println("Невідома команда: " + command);
        }
    }

    private void printGoals() {
        Map<Long, Goal> goals = storage.getGoals();

        if (goals.isEmpty()) {
            System.out.println("Цілей поки немає.");
            return;
        }

        for (Goal goal : goals.values()) {
            System.out.println(
                    goal.getId() + ": " + goal.getName()
                            + " — " + goal.getBalance()
                            + "/" + goal.getTargetAmount()
                            + ", досягнута: " + goal.isReached()
            );
        }
    }

    private void handleCreate(String arguments) {
        int separator = arguments.lastIndexOf(" ");

        if (separator == -1) {
            System.out.println("Формат: create <назва> <сума>");
            return;
        }

        String name = arguments.substring(0, separator);
        String amountText = arguments.substring(separator + 1);

        try {
            Long targetAmount = Long.parseLong(amountText);
            Goal goal = new Goal(name, targetAmount);
            storage.addGoal(goal);

            System.out.println("Ціль створена. ID: " + goal.getId());
        } catch (NumberFormatException e) {
            System.out.println("Сума має бути цілим числом, ви ввели: " + amountText);
        } catch (IllegalArgumentException e) {
            System.out.println("Не вдалося створити ціль: " + e.getMessage());
        }
    }

    private void handleDeposit(String arguments) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            System.out.println("Формат: deposit <id> <сума>");
            return;
        }

        try {
            long id = Long.parseLong(values[0]);
            long amount = Long.parseLong(values[1]);

            storage.findGoal(id).deposit(amount);
            System.out.println("ID цілі: " + id);
            System.out.println("Сума поповнення: " + amount);
        } catch (NumberFormatException e) {
            System.out.println("ID та сума мають бути цілими числами.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Не вдалося поповнити ціль: " + e.getMessage());
        }
    }

    private void handleWithdraw(String arguments) {
        String[] values = arguments.trim().split("\\s+");

        if (values.length != 2) {
            System.out.println("Формат: withdraw <id> <сума>");
            return;
        }

        try {
            long id = Long.parseLong(values[0]);
            long amount = Long.parseLong(values[1]);

            storage.findGoal(id).withdraw(amount);
            System.out.println("ID цілі: " + id);
            System.out.println("Сума зняття: " + amount);
        } catch (NumberFormatException e) {
            System.out.println("ID та сума мають бути цілими числами.");
        } catch (GoalNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Не вдалося зняти гроші з цілі: " + e.getMessage());
        }
    }


}
