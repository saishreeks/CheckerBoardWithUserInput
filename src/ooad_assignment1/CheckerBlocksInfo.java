package ooad_assignment1;

import java.awt.*;

/** stores the information of the checker blocks such as the x-y coordinates, row and column */

public class CheckerBlocksInfo {

    private int x;
    private int y;
    private int row;
    private int col;
    private Graphics g;


    public CheckerBlocksInfo(int x, int y, int row, int col, Graphics g) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
        this.g = g;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Graphics getGraphics() {
        return g;
    }


}
