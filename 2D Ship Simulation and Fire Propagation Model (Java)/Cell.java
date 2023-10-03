public class Cell {

    public static final int BLOCKED = 0;
    public static final int OPEN = 1;
    public static final int BOT = 2;
    public static final int BUTTON = 3;
    public static final int FIRE = 4;
    public static final int BOTONFIRE = 5;
    public static final int ESTINGUISHED = 6;

    private int state;

    public Cell(int initialState) {
        this.state = initialState;
    }

    public int getState() {
        return state;
    }

    public void setState(int newState) {
        this.state = newState;
    }
}
