package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Stack;

public class SquarePanel extends JPanel {
    int x = 0, y = 0;
    int dimension = 60;
    //numbers the dark blocks
    int number = 0;
    //decides to make a dark block/light block
    boolean isDark = true;
    //to check for even row
    boolean evenRow = true;

    CheckerBlocksInfo checkerBlocks;

    //stores the color of the checker piece along with the block number
    HashMap<Integer, Color> position = new HashMap<>();

    //stores the coordinates, row and column of each of the checker blocks
    HashMap<Integer, CheckerBlocksInfo> blockInfoMap = new HashMap<>();

    Stack<CheckerPieceColorInBlockNumber> stack = new Stack<>();


    //paint component creates the checker board with the initial placement of the checker pieces
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //in even rows make dark block followed by light block and repeat
                if (evenRow) {
                    if (isDark) {
                        number += 1;

                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x, y, dimension, dimension);

                        CheckerBlocksInfo temp = new CheckerBlocksInfo(x, y, i, j, g);
                        blockInfoMap.put(number, temp);

                        if (i < 3) {
                            drawCircle(g, x, y, Color.black);
                            position.put(number, Color.black);
                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red);
                            position.put(number, Color.red);
                        }

                        isDark = false;
                        x += dimension;
                        String s = String.valueOf(number);
                        g.setColor(Color.WHITE);
                        g.drawString(s, x - 15, y + 10);


                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, dimension, dimension);
                        isDark = true;
                        x += dimension;
                    }
                }
                //in odd rows make light block followed by dark block and repeat
                else {
                    if (!isDark) {
                        number += 1;
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x, y, dimension, dimension);

                        CheckerBlocksInfo temp = new CheckerBlocksInfo(x, y, i, j, g);
                        blockInfoMap.put(number, temp);

                        if (i < 3) {
                            drawCircle(g, x, y, Color.black);
                            position.put(number, Color.black);

                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red);
                            position.put(number, Color.red);
                        }

                        isDark = true;
                        x += dimension;


                        String s = String.valueOf(number);
                        g.setColor(Color.WHITE);
                        g.drawString(s, x - 15, y + 10);

                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, dimension, dimension);
                        isDark = false;
                        x += dimension;
                    }
                }


            }
            evenRow = !evenRow;
            y += dimension;
            x = 0;
        }

        for (int name : position.keySet()) {
            System.out.println(name + " : " + position.get(name));
        }
        System.out.println("12 :" + getValueInPosition(12));
        System.out.println("34 :" + getValueInPosition(34));

        for (int cb : blockInfoMap.keySet()) {
            System.out.println(cb + "at" + blockInfoMap.get(cb).getX());
        }

    }

    //draws the checker pieces in the required blocks
    private void drawCircle(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillOval(x + 10, y + 10, 40, 40);
    }

    //gives the color of the checker piece in the given position and returns null if there ain't any
    public Color getValueInPosition(int positionNumber) {

        Color value = null;
        if (position.containsKey(positionNumber)) {
            value = position.get(positionNumber);
        }
        return value;
    }

    public void moveDiagonal(Graphics g, int fromBlockNumber, int toBlockNumber) {
        moveDiagonal(g, blockInfoMap.get(fromBlockNumber), blockInfoMap.get(toBlockNumber), fromBlockNumber, toBlockNumber);
    }

    //Validates that the move-from block has a checker piece and move-to block doesn't and moves the checker piece
    public void moveDiagonal(Graphics x, CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock, int fromNum, int toNum) {

        CheckerPieceColorInBlockNumber colorInFromBlockNumber;
        CheckerPieceColorInBlockNumber colorInToBlockNumber;

        int row = fromBlock.getRow();
        int col = fromBlock.getCol();
        int toRow = toBlock.getRow();
        int toCol = toBlock.getCol();

        int fromX = fromBlock.getX();
        int fromY = fromBlock.getY();

        Color pieceColor = getValueInPosition(fromNum);


        if (!checkValidMove(fromNum, toNum, pieceColor)) {
            System.out.println("Not a valid move");
            return;
        }

        if (!hasCheckerPiece(fromNum)) {
            System.out.println("from " + fromNum + "has a no checker piece");
            return;
        }

        if (!isDiagonal(row, col, toRow, toCol)) {
            System.out.println(" they are not diagonal");
            return;
        }

        if (hasCheckerPiece(toNum)) {
            System.out.println("to: " + toNum + " has piece. can't move");
            return;
        }


        if (calculateDistance(row, col, toRow, toCol) != 1) {
            x.setColor(Color.DARK_GRAY);
            x.fillRect(((col + toCol) / 2) * dimension, ((row + toRow) / 2) * dimension, dimension, dimension);
        }


        int toX = toBlock.getX();
        int toY = toBlock.getY();
        Graphics g = toBlock.getGraphics();
        x.setColor(pieceColor);
        x.fillOval(toX + 10, toY + 10, 40, 40);

        //update the hash map by adding the checker piece in the move-to location and deleting it from the move-from location
        position.put(toNum, pieceColor);
        position.remove(fromNum);

        x.setColor(Color.DARK_GRAY);
        x.fillRect(fromX, fromY, dimension, dimension);

        //push the toBlock and fromBlock info to the stack
        System.out.println(g);
        colorInFromBlockNumber = new CheckerPieceColorInBlockNumber();
        colorInFromBlockNumber.setBlockNumber(fromNum);
        colorInFromBlockNumber.setColor(pieceColor);
        stack.push(colorInFromBlockNumber);

        colorInToBlockNumber = new CheckerPieceColorInBlockNumber();
        colorInToBlockNumber.setBlockNumber(toNum);
        colorInToBlockNumber.setColor(pieceColor);
        stack.push(colorInToBlockNumber);


    }

    private int calculateDistance(int row, int col, int toRow, int toCol) {
        return Math.max(Math.abs(row - toRow), Math.abs(col - toCol));
    }

    // red and black shouldn't move backwards
    private boolean checkValidMove(int fromNum, int toNum, Color pieceColor) {


        if (pieceColor.equals(Color.red))
            return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && fromNum > toNum : fromNum > toNum;

        return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && toNum > fromNum : toNum > fromNum;
    }

    //checks if the move-from and move-to blocks are diagonal
    public boolean isDiagonal(int row, int col, int toRow, int toCol) {
        boolean result = false;
        result = (Math.abs(row - toRow) == Math.abs(col - toCol));
        return result;
    }

    //checks if the block has a checker piece or not
    public boolean hasCheckerPiece(int blockNumber) {

        Color val = getValueInPosition(blockNumber);

        if (val == null)
            return false;
        else
            return true;


    }

    public void undo(Graphics g) {
        CheckerPieceColorInBlockNumber previousToBlock = stack.pop(); // gives to
        CheckerPieceColorInBlockNumber previousFromBlock = stack.pop(); //gives from
        CheckerBlocksInfo from = blockInfoMap.get(previousFromBlock.getBlockNumber());
        drawCircle(g, from.getX(), from.getY(), previousToBlock.getColor());

        CheckerBlocksInfo to = blockInfoMap.get(previousToBlock.getBlockNumber());
        g.setColor(Color.DARK_GRAY);
        g.fillRect(to.getX(),to.getY(),dimension,dimension);

        position.remove(previousToBlock.getBlockNumber());
        position.put(previousFromBlock.getBlockNumber(),previousFromBlock.getColor());


    }


}
