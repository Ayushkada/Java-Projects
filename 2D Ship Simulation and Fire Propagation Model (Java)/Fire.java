import java.util.*;

public class Fire {

    private Ship ship;
    private double q;
    public List<int[]> burningCells;

    public Fire(Ship ship, double q) {
        this.ship = ship;
        this.q = q;
        this.burningCells = new ArrayList<>();
    }

    public void addBurningCell(int x, int y) {
        burningCells.add(new int[] { x, y });
        ship.setCell(x, y, Cell.FIRE);
    }

    public List<int[]> spreadFire() {
        List<int[]> newBurningCells = new ArrayList<>();
        // get all open neighbors of each burning cell
        for (int[] burningCell : burningCells) {
            List<int[]> openNeighbors = ship.getOpenOrBotNeighbors(burningCell[0], burningCell[1]);
            // for each open neighbor, check if fire will spread to cell with probabilty of
            // 1 - (1 - q)^K
            // if so, set cell in ship to burning cell
            for (int[] openNeighbor : openNeighbors) {
                int K = ship.countBurningNeighbors(openNeighbor[0], openNeighbor[1]);
                double probability = 1 - Math.pow((1 - q), K);
                if (Math.random() < probability) {
                    newBurningCells.add(openNeighbor);
                    if (ship.getCell(openNeighbor[0], openNeighbor[1]).getState() == Cell.BOT) {
                        ship.setCell(openNeighbor[0], openNeighbor[1], Cell.BOTONFIRE);
                    } else {
                        ship.setCell(openNeighbor[0], openNeighbor[1], Cell.FIRE);
                    }
                }
            }
        }
        // add all new burning cells to current list of burning cells
        burningCells.addAll(newBurningCells);
        return burningCells;
    }
}
