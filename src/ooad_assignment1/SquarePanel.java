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
    HashMap<Integer, CheckerPieceInfo> position = new HashMap<>();


    //stores the coordinates, row and column of each of the checker blocks
    HashMap<Integer, CheckerBlocksInfo> blockInfoMap = new HashMap<>();

    Stack<CheckerPieceInfo> stack = new Stack<>();


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
                            position.put(number, new CheckerPieceInfo(number, Color.black, false));
                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red);
                            position.put(number, new CheckerPieceInfo(number, Color.red, false));
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
                            position.put(number, new CheckerPieceInfo(number, Color.black, false));

                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red);
                            position.put(number, new CheckerPieceInfo(number, Color.red, false));
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

    }

    //draws the checker pieces in the required blocks
    private void drawCircle(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillOval(x + 10, y + 10, 40, 40);
    }

    //gives the color of the checker piece in the given position and returns null if there ain't any
    public CheckerPieceInfo getValueInPosition(int positionNumber) {

        if (position.containsKey(positionNumber))
            return position.get(positionNumber);

        return null;

    }

    public void moveDiagonal(Graphics g, int fromBlockNumber, int toBlockNumber) {
        moveDiagonal(g, blockInfoMap.get(fromBlockNumber), blockInfoMap.get(toBlockNumber), fromBlockNumber, toBlockNumber);
    }

    //Validates that the move-from block has a checker piece and move-to block doesn't and moves the checker piece
    public void moveDiagonal(Graphics x, CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock, int fromNum, int toNum) {


        CheckerPieceInfo colorInFromBlockNumber;
        CheckerPieceInfo colorInToBlockNumber;

        int row = fromBlock.getRow();
        int col = fromBlock.getCol();
        int toRow = toBlock.getRow();
        int toCol = toBlock.getCol();

        int fromX = fromBlock.getX();
        int fromY = fromBlock.getY();

        Color pieceColor = getValueInPosition(fromNum).getColor();

        if (!hasCheckerPiece(fromNum)) {
            System.out.println("from " + fromNum + "has a no checker piece");
            return;
        }


        if (!checkValidMove(fromNum, toNum, pieceColor)) {
            System.out.println("Not a valid move");
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


        //if black is the first move then it returns to Checker frame and onclick of next it'll take next move ??????
        if (stack.empty() && !pieceColor.equals(Color.red)) {
            System.out.println("Player with red checkers should make the first move");
            return;
        }

/*if the distance between to and from block is more than 1, then it is a jump. Before jumping, it
checks if the middle Block had a checker piece in it. If its not there it shouldn't jump.
If there a piece in the middle block, then it'll jump over the block and removes the piece in the middle block */
        if (calculateDistance(row, col, toRow, toCol) != 1) {

            int midBlockNumber = 0;

            if ((row % 2 != 0) && (col % 2 != 0) && (toRow % 2 != 0) && (toCol % 2 != 0))
                midBlockNumber = (fromNum + toNum) / 2 + 1;
            else
                midBlockNumber = (fromNum + toNum) / 2;

            if (!hasCheckerPiece(midBlockNumber)) {
                System.out.println("There is no checker piece in " + midBlockNumber);
                return;
            }

            if (position.get(fromNum).getColor().equals(position.get(midBlockNumber).getColor())) {
                System.out.println(position.get(fromNum).getColor() + " can't jump on " + position.get(midBlockNumber).getColor());
                return;
            }

            x.setColor(Color.DARK_GRAY);
            x.fillRect(((col + toCol) / 2) * dimension, ((row + toRow) / 2) * dimension, dimension, dimension);
            //remove piece ??????
            position.remove(midBlockNumber);

        }


        int toX = toBlock.getX();
        int toY = toBlock.getY();
        Graphics g = toBlock.getGraphics();
        x.setColor(pieceColor);
        x.fillOval(toX + 10, toY + 10, 40, 40);

        CheckerPieceInfo checkerPieceInfo = position.get(fromNum);

        if (isCrowned(fromNum, toRow)) {
            System.out.println(position.get(fromNum).getColor() + " is crowned");
            x.fillOval(toX + 10, toY + 10, 40, 40);
            x.setColor(Color.white);
            x.drawString("K", toX + 30, toY + 30);
            checkerPieceInfo.setKing(true);
        } else
            x.fillOval(toX + 10, toY + 10, 40, 40);

        //update the hash map by adding the checker piece in the move-to location and deleting it from the move-from location


        checkerPieceInfo.setBlockNumber(toNum);
        position.put(toNum, checkerPieceInfo);
        position.remove(fromNum);

        x.setColor(Color.DARK_GRAY);
        x.fillRect(fromX, fromY, dimension, dimension);


        // to reprint the numbers not working????????
//        g.setColor(Color.WHITE);
//        g.drawString(String.valueOf(fromNum), fromX - 15, fromY + 10);


        //push the toBlock and fromBlock info to the stack
        colorInFromBlockNumber = new CheckerPieceInfo();
        colorInFromBlockNumber.setBlockNumber(fromNum);
        colorInFromBlockNumber.setColor(pieceColor);
        stack.push(colorInFromBlockNumber);

        colorInToBlockNumber = new CheckerPieceInfo();
        colorInToBlockNumber.setBlockNumber(toNum);
        colorInToBlockNumber.setColor(pieceColor);
        stack.push(colorInToBlockNumber);
    }


    private int calculateDistance(int row, int col, int toRow, int toCol) {
        return Math.max(Math.abs(row - toRow), Math.abs(col - toCol));
    }

    // red and black shouldn't move backwards and each player should get a chance to play
    private boolean checkValidMove(int fromNum, int toNum, Color pieceColor) {

        if (pieceColor.equals(Color.red))
            return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && fromNum > toNum : fromNum > toNum;

        return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && toNum > fromNum : toNum > fromNum;
    }

    //checks if the move-from and move-to blocks are diagonal
    private boolean isDiagonal(int row, int col, int toRow, int toCol) {
        return (Math.abs(row - toRow) == Math.abs(col - toCol));
    }

    //checks if the block has a checker piece or not
    private boolean hasCheckerPiece(int blockNumber) {

        CheckerPieceInfo val = getValueInPosition(blockNumber);

        if (val == null)
            return false;
        else
            return true;


    }

    private boolean isCrowned(int fromNum, int toRow) {

        if (position.get(fromNum).getColor().equals(Color.red) && toRow == 0 || position.get(fromNum).getColor().equals(Color.black) && toRow == 7)
            return true;
        return false;
    }

    public void undo(Graphics g) {

        Color color;
        if (stack.empty()) {
            System.out.println("didn't have any valid moves previously"); // no valid moves stored to undo ?????????
            return;
        }
        CheckerPieceInfo previousToBlock = stack.pop(); // gives to
        CheckerPieceInfo previousFromBlock = stack.pop(); //gives from
        CheckerBlocksInfo from = blockInfoMap.get(previousFromBlock.getBlockNumber());
        drawCircle(g, from.getX(), from.getY(), previousToBlock.getColor());

        CheckerBlocksInfo to = blockInfoMap.get(previousToBlock.getBlockNumber());
        g.setColor(Color.DARK_GRAY);
        g.fillRect(to.getX(), to.getY(), dimension, dimension);


        if (calculateDistance(from.getRow(), from.getCol(), to.getRow(), to.getCol()) != 1) {

            color = (previousFromBlock.getColor().equals(Color.red)) ? Color.black : Color.red;

            g.setColor(color);
            g.fillOval(((from.getCol() + to.getCol()) / 2) * dimension + 10, ((from.getRow() + to.getRow()) / 2) * dimension + 10, 40, 40);

            int midBlockNumber=0;
            if ((from.getRow() % 2 != 0) && (from.getCol() % 2 != 0) && (to.getRow() % 2 != 0) && (to.getCol() % 2 != 0))
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2 + 1;
            else
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2;

            //if centre is King not handled ??????????????
            position.put(midBlockNumber, new CheckerPieceInfo(midBlockNumber,color,false));
        }

        position.remove(previousToBlock.getBlockNumber());
        position.put(previousFromBlock.getBlockNumber(), previousFromBlock);


    }


}
