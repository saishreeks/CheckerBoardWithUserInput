package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
                            drawCircle(g, x, y, Color.black, false);
                            position.put(number, new CheckerPieceInfo(number, Color.black, false));
                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red, false);
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
                            drawCircle(g, x, y, Color.black, false);
                            position.put(number, new CheckerPieceInfo(number, Color.black, false));

                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red, false);
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
    private void drawCircle(Graphics g, int x, int y, Color color, boolean isKing) {
        if (isKing) {
            g.setColor(color);
            g.fillOval(x + 10, y + 10, 40, 40);
            g.setColor(Color.white);
            g.drawString("K", x + 30, y + 30);
        } else {
            g.setColor(color);
            g.fillOval(x + 10, y + 10, 40, 40);
        }
    }

    //gives the color of the checker piece in the given position and returns null if there ain't any
    public CheckerPieceInfo getValueInPosition(int positionNumber) {

        if (position.containsKey(positionNumber))
            return position.get(positionNumber);

        return null;

    }

    public List<Integer> checkForAvailableJumps() { List<Integer> validFromBlocksList = new ArrayList<>();
        if (stack.size() > 0) {
            Color color = stack.peek().getColor();
            Iterator it = position.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (position.get(pair.getKey()).getColor() != color) {
                    CheckerPieceInfo tempChecker = position.get(pair.getKey());
                    int tempBlockNumber = position.get(pair.getKey()).getBlockNumber();
                    if (checkForLegalJumps(tempChecker, blockInfoMap.get(tempBlockNumber))) {
                        validFromBlocksList.add(tempBlockNumber);
                    }
                }
            }
        }
        return validFromBlocksList;
    }

    private boolean checkForLegalJumps(CheckerPieceInfo tempChecker, CheckerBlocksInfo checkerBlocksInfo) {
        int row = checkerBlocksInfo.getRow();
        int col = checkerBlocksInfo.getCol();

        if (canJump(tempChecker.getColor(), row, col, row - 1, col + 1, row - 2, col + 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() - 7))
            return true;
        if (canJump(tempChecker.getColor(), row, col, row - 1, col - 1, row - 2, col - 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() - 9))
            return true;
        if (canJump(tempChecker.getColor(), row, col, row + 1, col + 1, row + 2, col + 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() + 9))
            return true;
        if (canJump(tempChecker.getColor(), row, col, row + 1, col - 1, row + 2, col - 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() + 7))
            return true;
        return false;
    }

    private boolean canJump(Color color, int r1, int c1, int r2, int c2, int r3, int c3, int fromBlock, int toBlock) {

        int middleBlock = calculateMiddleBlockNumber(r1, c1, r3, c3, fromBlock, toBlock);
        if (color.equals(Color.red)) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (r3 > r1)

                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (position.get(middleBlock).getColor().equals(Color.red))
                return false;
        }
        if (color.equals(Color.black)) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (r3 < r1)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (position.get(middleBlock).getColor().equals(Color.black))
                return false;
        }
        if (color.equals(Color.red) && position.get(fromBlock).isKing()) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (position.get(middleBlock).getColor().equals(Color.red))
                return false;
        }
        if (color.equals(Color.black) && position.get(fromBlock).isKing()) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (position.get(middleBlock).getColor().equals(Color.black))
                return false;
        }

        return true;

    }

    private int calculateMiddleBlockNumber(int row, int col, int toRow, int toCol, int fromBlock, int toBlock) {
        int middleBlock = 0;
        if ((row % 2 != 0) && (col % 2 != 0) && (toRow % 2 != 0) && (toCol % 2 != 0))
            middleBlock = (fromBlock + toBlock) / 2 + 1;
        else
            middleBlock = (fromBlock + toBlock) / 2;
        return middleBlock;
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

        //if there is not piece in the block return null
        Color pieceColor = getValueInPosition(fromNum) != null ? getValueInPosition(fromNum).getColor() : null;



        if (!hasCheckerPiece(fromNum)) {
            System.out.println("from " + fromNum + "has a no checker piece");
            return;
        }

        if (position.get(fromNum).isKing()) {
            if (!checkValidMoveForKing(pieceColor)) {
                System.out.println(" player should alternate");
                return;
            }
        } else {

            if (!checkValidMove(fromNum, toNum, pieceColor)) {
                System.out.println("Not a valid move");
                return;
            }
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

        /* if the distance between to and from block is more than 1, then it is a jump.
         * If the distance is 2 then its a single jump. If the distance is more than 2, then it's a multiple jumps.
         * Also if distance is more than 2 and if the distance is odd then jump is not possible
         * because there'll be even number of blocks in between from and to blocks*/

        if (calculateDistance(row, col, toRow, toCol) == 2) {
            singleJump(fromNum, toNum, row, col, toRow, toCol, x);

        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 == 0) {
            multipleJumps(fromNum, toNum, row, col, toRow, toCol, x);

        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 != 0) {
            System.out.println("Jump is not possible from" + fromNum + " to" + toNum);
            return;
        }


        int toX = toBlock.getX();
        int toY = toBlock.getY();


        CheckerPieceInfo checkerPieceInfo = position.get(fromNum);

//draw the checker piece in the ToBlock. if it has reached the other side make it a King, else just draw the piece in toBlock
        if (isCrowned(fromNum, toRow)) {
            System.out.println(position.get(fromNum).getColor() + " is crowned");
            x.setColor(pieceColor);
            x.fillOval(toX + 10, toY + 10, 40, 40);
            x.setColor(Color.white);
            x.drawString("K", toX + 30, toY + 30);
            checkerPieceInfo.setKing(true);
        } else {
            drawCircle(x, toX, toY, pieceColor, position.get(fromNum).isKing());
        }

        //update the hash map by adding the checker piece in the move-to location and deleting it from the move-from location
        checkerPieceInfo.setBlockNumber(toNum);
        position.put(toNum, checkerPieceInfo);
        position.remove(fromNum);

        //remove the checker piece from fromBlock
        x.setColor(Color.DARK_GRAY);
        x.fillRect(fromX, fromY, dimension, dimension);


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

    private void multipleJumps(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics x) {
        int tempToNum = 0;
        if (fromNum > toNum) {
            if (col < toCol) {
                tempToNum = fromNum - 7;

                intermediateJumps(fromNum, toNum, row, col, toRow, toCol, x, tempToNum);

            } else if (col > toCol) {
                tempToNum = fromNum - 9;

                intermediateJumps(fromNum, toNum, row, col, toRow, toCol, x, tempToNum);


            }

        } else {
            if (col < toCol) {
                tempToNum = fromNum + 9;
                intermediateJumps(fromNum, toNum, row, col, toRow, toCol, x, tempToNum);
            } else if (col > toCol) {
                tempToNum = fromNum + 7;
                intermediateJumps(fromNum, toNum, row, col, toRow, toCol, x, tempToNum);
            }

        }
    }


    private void intermediateJumps(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics x, int tempToNum) {
        singleJump(fromNum, tempToNum, row, col, blockInfoMap.get(tempToNum).getRow(), blockInfoMap.get(tempToNum).getCol(), x);
        position.put(tempToNum, new CheckerPieceInfo(tempToNum, position.get(fromNum).getColor(), false));
        singleJump(tempToNum, toNum, blockInfoMap.get(tempToNum).getRow(), blockInfoMap.get(tempToNum).getCol(), toRow, toCol, x);
        position.remove(tempToNum);
    }

   /* Before jumping, it
    checks if the middle Block had a checker piece in it. If its not there it shouldn't jump.
    If there a piece in the middle block, then it'll jump over the block and removes the piece in the middle block */

    private void singleJump(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics x) {
        //have a function for middle block. Use that .........................????
        int midBlockNumber = 0;

        if ((row % 2 != 0) && (col % 2 != 0) && (toRow % 2 != 0) && (toCol % 2 != 0))
            midBlockNumber = (fromNum + toNum) / 2 + 1;
        else
            midBlockNumber = (fromNum + toNum) / 2;

        if (!hasCheckerPiece(midBlockNumber)) {
            System.out.println("There is no checker piece in " + midBlockNumber + "can't jump on an empty block");
            return;
        }

        if (position.get(fromNum).getColor().equals(position.get(midBlockNumber).getColor())) {
            System.out.println(position.get(fromNum).getColor() + " can't jump on " + position.get(midBlockNumber).getColor());
            return;
        }

        //remove the piece from middle block
        x.setColor(Color.DARK_GRAY);
        x.fillRect(((col + toCol) / 2) * dimension, ((row + toRow) / 2) * dimension, dimension, dimension);

        position.remove(midBlockNumber);

    }

    private boolean checkValidMoveForKing(Color pieceColor) {
        return !pieceColor.equals(stack.peek().getColor());
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
        drawCircle(g, from.getX(), from.getY(), previousToBlock.getColor(), previousToBlock.isKing());

        CheckerBlocksInfo to = blockInfoMap.get(previousToBlock.getBlockNumber());
        g.setColor(Color.DARK_GRAY);
        g.fillRect(to.getX(), to.getY(), dimension, dimension);


        if (calculateDistance(from.getRow(), from.getCol(), to.getRow(), to.getCol()) != 1) {

            color = (previousFromBlock.getColor().equals(Color.red)) ? Color.black : Color.red;

            //draw the the circle in the middle (jump over piece)
            g.setColor(color);
            drawCircle(g, ((from.getCol() + to.getCol()) / 2) * dimension, ((from.getRow() + to.getRow()) / 2) * dimension, color, false);


            int midBlockNumber = 0;
            if ((from.getRow() % 2 != 0) && (from.getCol() % 2 != 0) && (to.getRow() % 2 != 0) && (to.getCol() % 2 != 0))
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2 + 1;
            else
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2;

            //if centre is King not handled ??????????????
            position.put(midBlockNumber, new CheckerPieceInfo(midBlockNumber, color, false));
        }

        position.remove(previousToBlock.getBlockNumber());
        position.put(previousFromBlock.getBlockNumber(), previousFromBlock);


    }


}
