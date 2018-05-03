package ooad_assignment1;

import java.awt.*;

/*stores the information of the checker blocks such as the x-y coordinates, row and column */

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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Graphics getGraphics() {
        return g;
    }

    public void setGraphics(Graphics graphics) {
        this.g = graphics;
    }





}
