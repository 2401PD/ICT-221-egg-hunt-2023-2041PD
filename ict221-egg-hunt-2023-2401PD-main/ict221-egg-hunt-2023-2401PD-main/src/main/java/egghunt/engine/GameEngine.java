package egghunt.engine;
import java.util.Random;
import java.util.Scanner;
public class GameEngine {

    /**
     * An example board to store the current game state.
     *
     * Note: depending on your game, you might want to change this from 'int' to String or something?
     */

    private String[][] map;

    /**Stores the X and Y co-ordinates of the player cell*/
    public MapPosition currentCell;

    /** Creates the random method*/
    private Random rand = new Random();

    private Scanner keyboard = new Scanner(System.in);

    /**List of constants*/
    private static final int MAX_DIFFICULTY = 10;
    private static final int MIN_DIFFICULTY = 0;
    private static final int DEFAULT_DIFFICULTY = 5;
    private static final int TOTAL_EGGS = 5;
    private static final int TOTAL_KEYS = 5;
    private static final String OBJECT_KEY = "K";
    private static final String OBJECT_EGG = "E";
    private static final String OBJECT_LOCK = "L";
    private static final String OBJECT_FINISH = "F";

    private static final String OBJECT_EMPTY = "_";
    private static final String PLAYER = "X";

    private static final String MOVE_UP = "w";
    private static final String MOVE_LEFT = "a";
    private static final String MOVE_DOWN = "s";
    private static final String MOVE_RIGHT = "d";
    private static final int REQUIRED_EGGS = 5;
    private static final int MAXIMUM_MOVES = 100;

    /**List of variables*/
    public int totalKeys = 0;
    public int totalEggs = 0;
    public int moveNumber = 0;
    boolean gameIsRunning = true;

    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {
        map = new String[size][size];
    }

    /**
     * The size of the current game.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * Plays a text-based game
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        int difficulty = engine.getDifficulty();engine.initialiseMap();
        engine.initialiseEngine(difficulty);
        engine.printMap();
        engine.mainGameLoop();
    }

    /**Prepares the text map to be populated with randomly placed cells*/
    private void initialiseMap() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                map[i][j] = OBJECT_EMPTY;
            }
        }
    }

    public void initialiseEngine(int difficulty) {
        this.initialiseMap();
        this.setStartAndFinish();
        this.randomiseObjects(OBJECT_KEY, TOTAL_KEYS);
        this.randomiseObjects(OBJECT_EGG, TOTAL_EGGS);
        this.randomiseObjects(OBJECT_LOCK, difficulty);
    }

    /**Selects a random cell on the map*/
    private MapPosition randomCell() {
        return new MapPosition(rand.nextInt(getSize()), rand.nextInt(getSize()));
    }

    /**Creates an empty cell on the map*/
    private boolean isEmpty(MapPosition cell) {
        return map[cell.y()] [cell.x()] == OBJECT_EMPTY;
    }

    /**This function randomises the object placement of eggs, locks and keys*/
    private void randomiseObjects(String objectName, int objectTotal) {
        int i = 0;
        while (i < objectTotal) {
            MapPosition cell = randomCell();
            if (isEmpty(cell)) {
                map[cell.y()] [cell.x()] = objectName;
                i++;
            }
        }
    }

    /**handles player interactions with specific cell types*/
    public String attemptToMoveTo(MapPosition newCell) {
        String message = "";
        if (newCell.y() < getSize() && newCell.x() < getSize() && newCell.y() >= 0 && newCell.x() >= 0) {
            String mapObject = map[newCell.y()][newCell.x()];
            boolean canMove = true;
            if (mapObject == OBJECT_LOCK) {
                if (totalKeys > 0){
                    totalKeys --;
                }
                else {
                    canMove = false;
                    message = "You need a key to move past a lock!";
                }
            }
            if (mapObject == OBJECT_EGG) {
                totalEggs ++;
            }
            if (mapObject == OBJECT_KEY) {
                totalKeys ++;
            }
            if (mapObject == OBJECT_FINISH) {
                if (totalEggs >= REQUIRED_EGGS) {
                    message = "You finished the game! Your score is " + (MAXIMUM_MOVES - moveNumber);
                    gameIsRunning = false;
                }
                else {
                    canMove = false;
                    message = "You need all " + TOTAL_EGGS + " eggs to enter this cell!";
                }
            }
            if (canMove) {
                moveBetween(currentCell, newCell);
                moveNumber ++;
            }
        }
        return message;
    }

    /**Asks the player to set a difficulty, which defaults to the default difficulty if an invalid response is selected*/
    private int getDifficulty() {
        System.out.println("Enter your difficulty (0-10). Default = " + DEFAULT_DIFFICULTY);
        String difficultyText = keyboard.nextLine();
        int difficulty;
        try {
            difficulty = Integer.valueOf(difficultyText);
        }
        catch (Exception e) {
            difficulty = DEFAULT_DIFFICULTY;
        }
        if (difficulty > MAX_DIFFICULTY || difficulty < MIN_DIFFICULTY) {
            difficulty = DEFAULT_DIFFICULTY;
        }
        return difficulty;
    }

    /**This function handles where the player starts and finishes each game*/
    private void setStartAndFinish() {
        map[0][0] = PLAYER;
        currentCell = new MapPosition(0, 0);
        map[getSize() - 1][getSize() - 1] = OBJECT_FINISH;
    }

    /**This function sets the destination as a player cell type, while the previous cell is turned into an empty cell*/
    public void moveBetween (MapPosition fromCell, MapPosition toCell) {
        map[fromCell.y()][fromCell.x()] = OBJECT_EMPTY;
        map[toCell.y()][toCell.x()] = PLAYER;
        currentCell = toCell;
    }

    /**This function updates the map after each input by the player*/
    private void printMap(){
        for (int i = getSize() - 1; i >= 0; i--) {
            for (int j = 0; j < getSize(); j++) {
                System.out.print(map[i][j]);
            }
            System.out.println("");
        }
    }

    public String mapAt(int x, int y) {
        return map[x][getSize() - y - 1];
    }

    /**This series of loops handles player inout, and feeds data into the relevant functions*/
    private void mainGameLoop() {
        while (gameIsRunning) {
            System.out.println("Enter a direction. Press the ENTER key to confirm.");
            System.out.println("Keybindings (case sensitive):");
            System.out.println("w = up, a = left, s = down, d = right. Type 'Exit' to end the game");
            String moveText = keyboard.nextLine();
            if (moveText.equals("Exit") || moveNumber >= MAXIMUM_MOVES) {
                System.out.println("You ran out of moves! Your score is -1");
                gameIsRunning = false;
        }
            else {
                String message = "";
                if (moveText.equals(MOVE_UP)) {
                    MapPosition newCell = MapPosition.upFrom(currentCell);
                    message = attemptToMoveTo(newCell);
                }
                if (moveText.equals(MOVE_RIGHT)) {
                    MapPosition newCell = MapPosition.rightFrom(currentCell);
                    message = attemptToMoveTo(newCell);
                }
                if (moveText.equals(MOVE_DOWN)) {
                    MapPosition newCell = MapPosition.downFrom(currentCell);
                    message = attemptToMoveTo(newCell);
                }
                if (moveText.equals(MOVE_LEFT)) {
                    MapPosition newCell = MapPosition.leftFrom(currentCell);
                    message = attemptToMoveTo(newCell);
                }
                if (!message.equals("")) {
                    System.out.println(message);
                }
                printMap();
            }
        }
    }
}

