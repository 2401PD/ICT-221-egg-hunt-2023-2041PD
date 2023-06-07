package egghunt.engine;

/**This class handles the X and Y positions of the player cell type*/
public class MapPosition {
    private int x;
    private int y;

    public MapPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int x(){
        return x;
    }
    public int y() {
        return y;
    }

    /**These functions increment or decrement the player cell's X or Y values*/
    public static MapPosition upFrom(MapPosition cell) {
        return new MapPosition(cell.x, cell.y + 1);
    }
    public static MapPosition downFrom(MapPosition cell) {
        return new MapPosition(cell.x, cell.y - 1);
    }
    public static MapPosition leftFrom(MapPosition cell) {
        return new MapPosition(cell.x - 1, cell.y );
    }
    public static MapPosition rightFrom(MapPosition cell) {
        return new MapPosition(cell.x + 1, cell.y);
    }
}
