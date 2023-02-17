import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {

    private final RobotMap map;
    private final Map<String, CommandExecutor> commands;

    public CommandManager(RobotMap map) {
        this.map = map;
        commands = new HashMap<>();
        commands.put("h", this::printHelp);
        commands.put("q", this::quit);
        commands.put("a", this::addRobot);
        commands.put("l", this::listRobots);
        commands.put("cd", this::changeDirectionRobot);

        // FIXME: 17.02.2023
    }

    public String handleCommand(String command) throws CommandNotFoundException, CommandExecutionException {
        String[] split = command.split(" ");
        String commandCode = split[0];

        CommandExecutor executor = commands.get(commandCode);
        if (executor == null) {
            throw new CommandNotFoundException(command);
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);
        return executor.execute(args);
    }

    private String addRobot(String[] args) throws CommandExecutionException {
        if (args.length != 2) {
            throw new CommandExecutionException("Недостаточно аргументов");
        }

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);

        try {
            map.createRobot(new Point(x, y));
        } catch (RobotCreationException e) {
            throw new CommandExecutionException(e.getMessage());
        }

        return "Робот " + map.getRobots().get(map.getRobots().size() - 1) + " добавлен";
    }

    private String listRobots(String[] args) throws CommandExecutionException {
        if (map.getRobots().size() == 0)
            throw new CommandExecutionException("Не создано не одного робота");

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < map.getRobots().size(); i++) {
            str.append(map.getRobots().get(i)).append("\n");
        }
        return str.toString();
    }

    private String printHelp(String[] args) {
        return """
                h     -> распечатать список допустимых команд (help)
                a 1 2 -> создать робота на позиции (1, 2) (add)
                l     -> распечатать всех роботов (list)
                m     -> перемещаем робота на 1 единицу вперед (move)
                cd    -> изменить направление робота (change direction)
                q     -> завершить программу (quit)
                """;
    }

    private String quit(String[] args) {
        System.exit(0);
        return "До встречи!";
    }

    private interface CommandExecutor {
        String execute(String[] args) throws CommandExecutionException;

    }

    private String moveRobot(String[] args) throws CommandExecutionException{
        if (map.getRobots().size() == 0)
            throw new CommandExecutionException("Не создано не одного робота");
        System.out.println(map.getRobots());

        return null;
    }

    private String changeDirectionRobot(String[] args) throws CommandExecutionException {
        Scanner sc = new Scanner(System.in);
        if (map.getRobots().size() == 0)
            throw new CommandExecutionException("Не создано не одного робота");

        System.out.println(map.getRobots());
        System.out.println("Выберете робота введя номер его позиции");
        int index = sc.nextInt() - 1;
        System.out.println("Выберете, куда повернуться роботу");
        String str = sc.nextLine();
        sc.close();


        return null;
    }



    private void homework() {
        // Доделать остальные команды move, change direction
    }
}
