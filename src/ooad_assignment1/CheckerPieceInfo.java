package ooad_assignment1;

import java.awt.*;

/** This class gives the information about the checker piece in a given block.
* It gives the color of the checker piece in a given block number and states whether it is a king or not */

public class CheckerPieceInfo {

    private int blockNumber;
    private Color color;
    private boolean isKing;

    public CheckerPieceInfo(){}

    public CheckerPieceInfo(int blockNumber, Color color, boolean isKing) {
        this.blockNumber = blockNumber;
        this.color = color;
        this.isKing = isKing;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }



}
