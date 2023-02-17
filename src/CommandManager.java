import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        commands.put("m", this::moveRobot);

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
                h                  -> распечатать список допустимых команд (help)
                a 1 2              -> создать робота на позиции (1, 2) (add)
                l                  -> распечатать всех роботов (list)
                m index [t] [5]    -> перемещаем робота на заданное количество единиц и направление (move)
                q                  -> завершить программу (quit)
                """;
    }

    private String quit(String[] args) {
        System.exit(0);
        return "До встречи!";
    }

    private interface CommandExecutor {
        String execute(String[] args) throws CommandExecutionException;

    }

    private String moveRobot(String[] args) throws CommandExecutionException {
        if (args.length > 3 || args.length < 2)
            throw new CommandExecutionException("Недостаточно аргументов");

        if (map.getRobots().size() == 0)
            throw new CommandExecutionException("Не создано не одного робота");

        int index = Integer.parseInt(args[0]) - 1;
        String dir = "";
        switch (String.valueOf(args[1])) {
            case "t" -> dir = "TOP";
            case "b" -> dir = "BOTTOM";
            case "r" -> dir = "RIGHT";
            case "l" -> dir = "LEFT";
        }

        Direction direction = Direction.valueOf(dir);
        RobotMap.Robot robot = map.getRobots().get(index);
        robot.changeDirection(direction);
        if (args.length == 2) {
            try {
                robot.move();
            } catch (RobotMoveException e) {
                System.err.println(e.getMessage());
            }
        } else {
            int step = Integer.parseInt(args[2]);
            try {
                robot.move(step);
            } catch (RobotMoveException e) {
                System.err.println(e.getMessage());
            }
        } return "Теперь другие координаты у " + robot;
    }

    private void homework() {
        // Доделать остальные команды move, change direction
    }
}
