import java.util.*;

public class Ship {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_ORANGE = "\u001B[38;5;208m";
    public static final String ANSI_GRAY = "\u001B[90m";
    public static final String ANSI_BLACK = "\u001B[30m";


    private Cell[][] shipGrid;

    public Ship(int d) {
        shipGrid = new Cell[d][d];
        for (int i = 0; i < shipGrid.length; i++) {
            for (int j = 0; j < shipGrid[i].length; j++) {
                shipGrid[i][j] = new Cell(Cell.BLOCKED);
            }
        }
    }

    public Cell[][] getShipGrid() {
        return shipGrid;
    }

    public Cell getCell(int x, int y) {
        return shipGrid[x][y];
    }

    public void setShipGrid(Cell[][] shipGrid) {
        this.shipGrid = shipGrid;
    }

    public void setCell(int x, int y, int newState) {
        shipGrid[x][y].setState(newState);
    }

    public void generateShip() {

        // choose random cell in grid and set it to open
        Random rand = new Random();
        int x = rand.nextInt(shipGrid.length);
        int y = rand.nextInt(shipGrid.length);
        shipGrid[x][y].setState(Cell.OPEN);

        // open random cells with exactly one open neighbor until cannot anymore
        boolean canOpenMoreCells = true;
        while (canOpenMoreCells) {
            // printShip();
            // Find all blocked cells with exactly one open neighbor and put them into list
            ArrayList<int[]> canOpen = new ArrayList<>();
            for (int i = 0; i < shipGrid.length; i++) {
                for (int j = 0; j < shipGrid.length; j++) {
                    if (shipGrid[i][j].getState() == Cell.BLOCKED && countOpenNeighbors(i, j) == 1) {
                        canOpen.add(new int[] { i, j });
                    }
                }
            }
            if (canOpen.isEmpty())
                canOpenMoreCells = false;
            else {
                // Choose cell at random from list to open
                int[] randomCell = canOpen.get(rand.nextInt(canOpen.size()));
                shipGrid[randomCell[0]][randomCell[1]].setState(Cell.OPEN);
            }
        }

        // identify all dead end cells
        ArrayList<int[]> deadEnd = new ArrayList<>();
        for (int i = 0; i < shipGrid.length; i++) {
            for (int j = 0; j < shipGrid.length; j++) {
                if (shipGrid[i][j].getState() == Cell.OPEN && countBlockedNeighbors(i, j) == 3) {
                    deadEnd.add(new int[] { i, j });
                }
            }
        }
        // System.out.println(deadEnd.size());
        // open random closed neighbor of half of dead end cells
        double count = 0;
        while (count < (deadEnd.size() / 2.0)) {
            List<int[]> blockedNeighbors = new ArrayList<>();
            int[] randCell = deadEnd.get(rand.nextInt(deadEnd.size()));
            blockedNeighbors = getBlockedNeighbors(randCell[0], randCell[1]);
            if (!blockedNeighbors.isEmpty()) {
                int[] chosenCell = blockedNeighbors.get(rand.nextInt(blockedNeighbors.size()));
                shipGrid[chosenCell[0]][chosenCell[1]].setState(Cell.OPEN);
            }
            // printShip();
            count++;
        }
    }

    public List<int[]> getBlockedNeighbors(int x, int y) {
        List<int[]> blockedNeighbors = new ArrayList<>();
        int[][] neighbors = { { x + 1, y }, { x - 1, y }, { x, y - 1 }, { x, y + 1 } };
        for (int[] cord : neighbors) {
            int tempX = cord[0];
            int tempY = cord[1];
            if (tempX >= 0 && tempX < shipGrid.length && tempY >= 0 && tempY < shipGrid[0].length) {
                if (shipGrid[tempX][tempY].getState() == Cell.BLOCKED) {
                    blockedNeighbors.add(new int[] { tempX, tempY });
                }
            }
        }
        return blockedNeighbors;
    }

    public int countBlockedNeighbors(int x, int y) {
        return getBlockedNeighbors(x, y).size();
    }

    public List<int[]> getOpenNeighbors(int x, int y) {
        List<int[]> openNeighbors = new ArrayList<>();
        int[][] neighbors = { { x + 1, y }, { x - 1, y }, { x, y - 1 }, { x, y + 1 } };
        for (int[] cord : neighbors) {
            int tempX = cord[0];
            int tempY = cord[1];
            if (tempX >= 0 && tempX < shipGrid.length && tempY >= 0 && tempY < shipGrid[0].length) {
                if (shipGrid[tempX][tempY].getState() == Cell.OPEN) {
                    openNeighbors.add(new int[] { tempX, tempY });
                }
            }
        }
        return openNeighbors;
    }

    public int countOpenNeighbors(int x, int y) {
        return getOpenNeighbors(x, y).size();
    }

    public int[] getRandomOpenCell() {
        List<int[]> openCells = new ArrayList<>();
        for (int i = 0; i < shipGrid.length; i++) {
            for (int j = 0; j < shipGrid.length; j++) {
                if (shipGrid[i][j].getState() == Cell.OPEN) {
                    openCells.add(new int[] { i, j });
                }
            }
        }
        Random rand = new Random();
        return openCells.get(rand.nextInt(openCells.size()));
    }

    public void printShip() {
        int count = 0;
        while (count < shipGrid.length * 2.5) {
            System.out.print("_");
            count++;
        }
        System.out.print("\n");
        for (int i = 0; i < shipGrid.length; i++) {
            System.out.print("|");
            for (int j = 0; j < shipGrid.length; j++) {
                int state = getCell(i, j).getState();
                switch (state) {
                    case Cell.BLOCKED:
                        System.out.print(ANSI_BLACK + state + ANSI_RESET + " ");
                        continue;
                    case Cell.BOT:
                        System.out.print(ANSI_BLUE + state + ANSI_RESET + " ");
                        continue;
                    case Cell.BUTTON:
                        System.out.print(ANSI_YELLOW + state + ANSI_RESET + " ");
                        continue;
                    case Cell.FIRE:
                        System.out.print(ANSI_RED + state + ANSI_RESET + " ");
                        continue;
                    case Cell.BOTONFIRE:
                        System.out.print(ANSI_ORANGE + state + ANSI_RESET + " ");
                        continue;
                    case Cell.ESTINGUISHED:
                        System.out.print(ANSI_GREEN + state + ANSI_RESET + " ");
                        continue;
                    default:
                        System.out.print(state + " ");
                        continue;
                }
            }
            System.out.print("|\n");
        }
        count = 0;
        while (count < shipGrid.length * 2.5) {
            System.out.print("-");
            count++;
        }
        System.out.println("\n");
    }

    public List<int[]> getBurningNeighbors(int x, int y) {
        List<int[]> burningNeighbors = new ArrayList<>();
        int[][] neighbors = { { x + 1, y }, { x - 1, y }, { x, y - 1 }, { x, y + 1 } };
        for (int[] cord : neighbors) {
            int tempX = cord[0];
            int tempY = cord[1];
            if (tempX >= 0 && tempX < shipGrid.length && tempY >= 0 && tempY < shipGrid[0].length) {
                if (shipGrid[tempX][tempY].getState() == Cell.FIRE) {
                    burningNeighbors.add(new int[] { tempX, tempY });
                }
            }
        }
        return burningNeighbors;
    }

    public int countBurningNeighbors(int x, int y) {
        return getBurningNeighbors(x, y).size();
    }

    public List<int[]> getUnblockedNeighbors(int x, int y) {
        List<int[]> unblockedNeighbors = new ArrayList<>();
        int[][] neighbors = { { x + 1, y }, { x - 1, y }, { x, y - 1 }, { x, y + 1 } };
        for (int[] cord : neighbors) {
            int tempX = cord[0];
            int tempY = cord[1];
            if (tempX >= 0 && tempX < shipGrid.length && tempY >= 0 && tempY < shipGrid[0].length) {
                if (shipGrid[tempX][tempY].getState() != Cell.BLOCKED) {
                    unblockedNeighbors.add(new int[] { tempX, tempY });
                }
            }
        }
        return unblockedNeighbors;
    }

    public List<int[]> getOpenOrBotNeighbors(int x, int y) {
        List<int[]> openOrBotNeighbors = new ArrayList<>();
        int[][] neighbors = { { x + 1, y }, { x - 1, y }, { x, y - 1 }, { x, y + 1 } };
        for (int[] cord : neighbors) {
            int tempX = cord[0];
            int tempY = cord[1];
            if (tempX >= 0 && tempX < shipGrid.length && tempY >= 0 && tempY < shipGrid[0].length) {
                if (shipGrid[tempX][tempY].getState() == Cell.OPEN || shipGrid[tempX][tempY].getState() == Cell.BOT) {
                    openOrBotNeighbors.add(new int[] { tempX, tempY });
                }
            }
        }
        return openOrBotNeighbors;
    }

    public int[] getBotCell() {
        for (int i = 0; i < shipGrid.length; i++) {
            for (int j = 0; j < shipGrid.length; j++) {
                if (getCell(i, j).getState() == Cell.BOT || getCell(i, j).getState() == Cell.BOTONFIRE) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 };
    }
}