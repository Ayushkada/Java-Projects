import java.util.*;

public class Main {
    public static void main(String[] args) {
        int bot_number = 1;
        int timestep = Integer.MAX_VALUE;
        int D = 20;
        double q = .5;
        Ship ship = new Ship(D);
        ship.generateShip();

        Bot bot = new Bot(ship);
        Fire fire = new Fire(ship, q);

        // ship.printShip();

        int [] intialBotCell = ship.getRandomOpenCell();
        ship.setCell(intialBotCell[0], intialBotCell[1], Cell.BOT);

        int [] initialFireCell = ship.getRandomOpenCell();
        fire.addBurningCell(initialFireCell[0], initialFireCell[1]);

        int [] buttonCell = ship.getRandomOpenCell();
        ship.setCell(buttonCell[0], buttonCell[1], Cell.BUTTON);

        ship.printShip();



        int i = 1;
        while(i <= timestep){
            List<int[]> prevBurningCells = new ArrayList<>();
            prevBurningCells.addAll(fire.spreadFire());
            int x = bot.move(bot_number, intialBotCell, initialFireCell, prevBurningCells, buttonCell);
            ship.printShip();
            if(x == 1){
                System.out.println("Bot has been engulfed in fire in " + i + " moves");
                break;
            }
            if(x == 2){
                System.out.println("Bot has estinguished fire in " + i + " moves");
                break;
            }
            if(x == 3){
                System.out.println("No Path Found");
                break;
            }
            i++;
        }
    }
}
