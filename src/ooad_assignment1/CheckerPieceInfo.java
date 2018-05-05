package ooad_assignment1;

import java.awt.*;

public class CheckerPieceInfo {

    private int blockNumber;
    private Color checkerPieceColor;
    boolean isKing;

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Color getCheckerPieceColor() {
        return checkerPieceColor;
    }

    public void setCheckerPieceColor(Color checkerPieceColor) {
        this.checkerPieceColor = checkerPieceColor;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

}
