import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Есть двумерная карта с роботами
        // Роботы это объекты, они могут перемещаться по карте
        // Карта имеет какие-то границы
        // 2 робота не могут находиться в одной точке одновременно
        // Роботы имеют направление (которое можно менять (поворачивать)),
        //              роботы идут только вперед (или стоят на месте).


        Scanner userInput = new Scanner(System.in);

        System.out.println("\n" + """
                Привет! Давай поиграем в игру!
                
                Нужно создать карту и роботов, которых можно передвигать по карте!
                Для начала давай введем размер карты!
                """);

        RobotMap map;
        while (true) {
            System.out.println("Для создания карты введите 2 числа в диапозоне от 10 до 100 через пробел" + "\n");

            try {
                int n = userInput.nextInt();
                int m = userInput.nextInt();
                userInput.nextLine();

                map = new RobotMap(n, m);
                break;
            } catch (RobotMapCreationException e) {
                System.err.println("\n" + "Возникла ошибка при создании карты: " + e.getMessage() + "\n");
            } catch (InputMismatchException e) {
                System.err.println("\n" + "Возникла ошибка: введены не числа" + "\n");
                userInput.nextLine();
            } catch (Throwable e) {
                System.err.println("\n" + "Возникла проблема на стороне сервера: " + e.getMessage() + "\n");
                System.exit(1);
            }
        }

        System.out.println("Карта успешно создана" + "\n");

        CommandManager commandManager = new CommandManager(map);

        System.out.println("Для просмотра списка допустимых команд введите h" + "\n");


        while (true) {
            System.out.println("Введите команду");
            String command = userInput.nextLine();
            try {
                String commandExecutionResult = commandManager.handleCommand(command);
                if (Objects.nonNull(commandExecutionResult) && !commandExecutionResult.isBlank()) {
                    System.out.println(commandExecutionResult);
                }
            } catch (CommandNotFoundException e) {
                System.err.println("Команда [" + e.getMessage() + "] не найдена" + "\n");
            } catch (CommandExecutionException e) {
                System.err.println("Во время исполнения команды произошла ошибка: " + e.getMessage() + "\n");
            }
        }
    }
}