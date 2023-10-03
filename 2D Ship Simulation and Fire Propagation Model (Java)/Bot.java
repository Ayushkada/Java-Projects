import java.util.*;

public class Bot {
    private Ship ship;
    private int[] currentBotCell;
    int[] currState;
    Stack<int[]> stackBot1 = new Stack<>();

    public Bot(Ship ship) {
        this.ship = ship;
    }

    public int move(int bot_number, int[] intialBotCell, int[] initialFireCell, List<int[]> prevBurningCells,
            int[] buttonCell) {
        currentBotCell = ship.getBotCell();
        int shipCellValue = Cell.OPEN;
        int[] nextCell = null;

        if (bot_number == 1) {
            //since bot1 will not adapt to the fire, only perform DFS once for each ship model 
            if (Arrays.equals(currentBotCell, intialBotCell)) {
                List<int[]> intialFire = new ArrayList<>();
                intialFire.add(initialFireCell);
                stackBot1 = bfs(currentBotCell, intialFire, buttonCell);
            }
            //finds next cell avaliable and sets nextCell to that value
            if (!stackBot1.isEmpty()) {
                nextCell = stackBot1.pop();
            } else {
                return 3;
            }
            shipCellValue = ship.getCell(nextCell[0], nextCell[1]).getState();
        }
        if (bot_number == 2) {
            //perfom dfs each time to adapt to spread of fire
            Stack<int[]> path = bfs(currentBotCell, prevBurningCells, buttonCell);
            if (!path.isEmpty()) {
                nextCell = path.pop();
            } else {
                return 3;
            }
            shipCellValue = ship.getCell(nextCell[0], nextCell[1]).getState();
        }
        if (bot_number == 3) {
            //creates list of coordinates that are openNeighbors of current fire cells
            List<int[]> avoidCells = new ArrayList<>();
            avoidCells.addAll(prevBurningCells);

            for (int[] fireCell : prevBurningCells) {
                for (int[] openNeighbor : ship.getOpenNeighbors(fireCell[0], fireCell[1])) {
                    avoidCells.add(new int[] { openNeighbor[0], openNeighbor[1] });
                }
            }

            Stack<int[]> path = bfs(currentBotCell, avoidCells, buttonCell);
            if (!path.isEmpty()) {
                nextCell = path.pop();
            } else {
                //if there is no path to the button with avoiding fire cells and those adjacent, find path avoiding only fire cells
                Stack<int[]> path2 = bfs(currentBotCell, prevBurningCells, buttonCell);
                if (!path2.isEmpty()) {
                    nextCell = path2.pop();
                } else {
                    return 3;
                }
            }
            shipCellValue = ship.getCell(nextCell[0], nextCell[1]).getState();
        }

        //if fire has spread to where the bot currently is and it cannot move to a safe cell in its path, the bot is engulfed in fire
        if (ship.getCell(currentBotCell[0], currentBotCell[1]).getState() == Cell.BOTONFIRE
                && shipCellValue == Cell.FIRE) {
            ship.setCell(currentBotCell[0], currentBotCell[1], Cell.OPEN);
            ship.setCell(nextCell[0], nextCell[1], Cell.BOTONFIRE);
            return 1;
        }
        //if the next cell in the bots path is a fire cell, the bot will be engulfed in fire
        if (shipCellValue == Cell.FIRE) {
            ship.setCell(currentBotCell[0], currentBotCell[1], Cell.OPEN);
            ship.setCell(nextCell[0], nextCell[1], Cell.BOTONFIRE);
            return 1;
        }
        //if the bot finds the button, it estinguishes the fire
        if (shipCellValue == Cell.BUTTON) {
            ship.setCell(currentBotCell[0], currentBotCell[1], Cell.OPEN);
            ship.setCell(nextCell[0], nextCell[1], Cell.ESTINGUISHED);
            return 2;
        } else {
            ship.setCell(currentBotCell[0], currentBotCell[1], Cell.OPEN);
            ship.setCell(nextCell[0], nextCell[1], Cell.BOT);
            return 0;
        }
    }

    private Stack<int[]> bfs(int[] start, List<int[]> fire, int[] target) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(start);
        Map<String, int[]> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        visited.add(Arrays.toString(start));
        prev.put(Arrays.toString(start), null);

        while (!queue.isEmpty()) {
            int[] currState = queue.remove();
            if (Arrays.equals(currState, target)) {
                break;
            }
            //checks for the cells valid neighbors and adds them to queue
            List<int[]> unBlockedCells = ship.getUnblockedNeighbors(currState[0], currState[1]);
            for (int[] unBlockedCell : unBlockedCells) {
                boolean isCellOnFire = false;
                for (int[] fireCell : fire) {
                    if (Arrays.equals(unBlockedCell, fireCell)) {
                        isCellOnFire = true;
                        break;
                    }
                }
                String key = Arrays.toString(unBlockedCell);
                if (!visited.contains(key) && !isCellOnFire) {
                    queue.add(unBlockedCell);
                    visited.add(key);
                    prev.put(key, currState);
                }
            }
        }
        //retraces path back from target cell to current cell
        Stack<int[]> path = new Stack<>();
        int[] currState = target;
        while (prev.get(Arrays.toString(currState)) != null) {
            path.add(currState);
            currState = prev.get(Arrays.toString(currState));
        }
        return path;
    }

}
