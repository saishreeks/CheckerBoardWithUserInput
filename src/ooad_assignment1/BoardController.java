package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardController extends JPanel {
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
    HashMap<Integer, CheckerPieceInfo> checkerPieceInfoHashMap = new HashMap<>();


    //stores the coordinates, row and column of each of the checker blocks
    HashMap<Integer, CheckerBlocksInfo> blockInfoMap = new HashMap<>();

    Stack<CheckerPieceInfo> stack = new Stack<>();

    Stack<Integer> jumpMadeStack = new Stack<>();


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
                            checkerPieceInfoHashMap.put(number, new CheckerPieceInfo(number, Color.black, false));
                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red, false);
                            checkerPieceInfoHashMap.put(number, new CheckerPieceInfo(number, Color.red, false));
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
                            checkerPieceInfoHashMap.put(number, new CheckerPieceInfo(number, Color.black, false));

                        }

                        if (i > 4) {
                            drawCircle(g, x, y, Color.red, false);
                            checkerPieceInfoHashMap.put(number, new CheckerPieceInfo(number, Color.red, false));
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

    //gives the color of the checker piece in the given checkerPieceInfoHashMap and returns null if there ain't any
    public CheckerPieceInfo getValueInPosition(int positionNumber) {

        if (checkerPieceInfoHashMap.containsKey(positionNumber))
            return checkerPieceInfoHashMap.get(positionNumber);

        return null;

    }

    public List<Integer> checkForAvailableJumps() {
        List<Integer> validFromBlocksList = new ArrayList<>();
        if (stack.size() > 0) {
            Color color = stack.peek().getColor();
            Iterator it = checkerPieceInfoHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (checkerPieceInfoHashMap.get(pair.getKey()).getColor() != color) {
                    CheckerPieceInfo tempChecker = checkerPieceInfoHashMap.get(pair.getKey());
                    int tempBlockNumber = checkerPieceInfoHashMap.get(pair.getKey()).getBlockNumber();
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
            if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red))
                return false;
            if (hasCheckerPiece(toBlock))
                return false;
        }
        if (color.equals(Color.black)) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (r3 < r1)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                return false;
            if (hasCheckerPiece(toBlock))
                return false;
        }
        if (color.equals(Color.red) && checkerPieceInfoHashMap.get(fromBlock).isKing()) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red))
                return false;
            if (hasCheckerPiece(toBlock))
                return false;
        }
        if (color.equals(Color.black) && checkerPieceInfoHashMap.get(fromBlock).isKing()) {
            if (r2 < 0 || r2 > 8)
                return false;
            if (!hasCheckerPiece(middleBlock))
                return false;
            if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                return false;
            if (hasCheckerPiece(toBlock))
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
    public void moveDiagonal(Graphics graphics, CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock, int fromNum, int toNum) {


        CheckerPieceInfo colorInFromBlockNumber;
        CheckerPieceInfo colorInToBlockNumber;

        int row = fromBlock.getRow();
        int col = fromBlock.getCol();
        int toRow = toBlock.getRow();
        int toCol = toBlock.getCol();
        int fromX = fromBlock.getX();
        int fromY = fromBlock.getY();

        boolean testForCompleteJump = false;
        //if there is not piece in the block return null
        Color pieceColor = getValueInPosition(fromNum) != null ? getValueInPosition(fromNum).getColor() : null;

        if (calculateDistance(row, col, toRow, toCol) == 1) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }

            if (!isDiagonal(row, col, toRow, toCol)) {
                System.out.println("It is not a diagonal");
                return;
            }
        }

        /* if the distance between to and from block is more than 1, then it is a jump.
         * If the distance is 2 then its a single jump. If the distance is more than 2, then it's a multiple jumps.
         * Also if distance is more than 2 and if the distance is odd then jump is not possible
         * because there'll be even number of blocks in between from and to blocks*/

        else if (calculateDistance(row, col, toRow, toCol) == 2) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }

            if (!isDiagonal(row, col, toRow, toCol)) {
                System.out.println("It is not a diagonal");
                return;
            }
            singleJump(fromNum, toNum, row, col, toRow, toCol, graphics);
            testForCompleteJump = true;


        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 == 0) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }
            fromNum = multipleJumps(fromNum, toNum, row, col, toRow, toCol, graphics);

        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 != 0) {
            System.out.println("Jump is not possible from" + fromNum + " to" + toNum);
            return;
        }


        int toX = toBlock.getX();
        int toY = toBlock.getY();


        CheckerPieceInfo checkerPieceInfo = checkerPieceInfoHashMap.get(fromNum);

//draw the checker piece in the ToBlock. if it has reached the other side make it a King, else just draw the piece in toBlock
        if (isCrowned(fromNum, toRow)) {
            System.out.println(checkerPieceInfoHashMap.get(fromNum).getColor() + " is crowned");
            graphics.setColor(pieceColor);
            graphics.fillOval(toX + 10, toY + 10, 40, 40);
            graphics.setColor(Color.white);
            graphics.drawString("K", toX + 30, toY + 30);
            checkerPieceInfo.setKing(true);
        } else {
            drawCircle(graphics, toX, toY, pieceColor, checkerPieceInfoHashMap.get(fromNum).isKing());
        }

        //update the hash map by adding the checker piece in the move-to location and deleting it from the move-from location
        checkerPieceInfo.setBlockNumber(toNum);
        checkerPieceInfoHashMap.remove(fromNum);
        checkerPieceInfoHashMap.put(toNum, checkerPieceInfo);

        //remove the checker piece from fromBlock
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(fromX, fromY, dimension, dimension);


        //push the toBlock and fromBlock info to the stack
        colorInFromBlockNumber = new CheckerPieceInfo();
        colorInFromBlockNumber.setBlockNumber(fromNum);
        colorInFromBlockNumber.setColor(pieceColor);
        stack.push(colorInFromBlockNumber);

        colorInToBlockNumber = new CheckerPieceInfo();
        colorInToBlockNumber.setBlockNumber(toNum);
        colorInToBlockNumber.setColor(pieceColor);
        stack.push(colorInToBlockNumber);

        if (testForCompleteJump && checkForLegalJumps(checkerPieceInfoHashMap.get(toNum), blockInfoMap.get(toNum))) {
            System.out.println("jump not complete");
            undo(getGraphics());
            testForCompleteJump = false;
        }


    }

    private boolean validateConditions(int fromNum, int toNum, Color pieceColor) {

        if (!hasCheckerPiece(fromNum)) {
            System.out.println("from " + fromNum + "has a no checker piece");
            return false;
        }

        if (checkerPieceInfoHashMap.get(fromNum).isKing()) {
            if (!checkValidMoveForKing(pieceColor)) {
                System.out.println(" player should alternate");
                return false;
            }
        } else {

            if (!checkValidMove(fromNum, toNum, pieceColor)) {
                System.out.println("Not a valid move");
                return false;
            }
        }


        if (hasCheckerPiece(toNum)) {
            System.out.println("to: " + toNum + " has piece. can't move");
            return false;
        }


        //if black is the first move then it returns to Checker frame and onclick of next it'll take next move ??????
        if (stack.empty() && !pieceColor.equals(Color.red)) {
            System.out.println("Player with red checkers should make the first move");
            return false;
        }
        return true;
    }


    private int multipleJumps(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics graphics) {

        int tempToNum = 0;
        int jumpCount = 0;
        while (checkForLegalJumps(checkerPieceInfoHashMap.get(fromNum), blockInfoMap.get(fromNum))) {
            if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.red)) {

                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col + 1, row - 2, col + 2, fromNum, fromNum - 7)) {
                    tempToNum = fromNum - 7;
                    makeTempJump(fromNum, tempToNum, graphics);


                }
                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col - 1, row - 2, col - 2, fromNum, fromNum - 9)) {
                    tempToNum = fromNum - 9;
                    makeTempJump(fromNum, tempToNum, graphics);
                }
            } else if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.black)) {
                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col + 1, row + 2, col + 2, fromNum, fromNum + 9)) {
                    tempToNum = fromNum + 9;
                    makeTempJump(fromNum, tempToNum, graphics);
                }

                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col - 1, row + 2, col - 2, fromNum, fromNum + 7)) {
                    tempToNum = fromNum + 7;
                    makeTempJump(fromNum, tempToNum, graphics);
                }
            }
            jumpCount++;
            fromNum = tempToNum;


        }
        if (tempToNum == toNum) {
            checkerPieceInfoHashMap.put(toNum, new CheckerPieceInfo(toNum, checkerPieceInfoHashMap.get(fromNum).getColor(), checkerPieceInfoHashMap.get(fromNum).isKing()));
            return fromNum;
        } else {
            System.out.println("Jump not possible. Invalid input");
            jumpMadeStack.clear();
            return fromNum;
        }


    }

    private void makeTempJump(int fromNum, int tempToNum, Graphics graphics) {
        singleJump(fromNum, tempToNum, blockInfoMap.get(fromNum).getRow(), blockInfoMap.get(fromNum).getCol(), blockInfoMap.get(tempToNum).getRow(), blockInfoMap.get(tempToNum).getCol(), graphics);

        //push the temporary jump to the stack and update the other stack
        jumpMadeStack.push(tempToNum);
        checkerPieceInfoHashMap.put(tempToNum, new CheckerPieceInfo(tempToNum, checkerPieceInfoHashMap.get(fromNum).getColor(), checkerPieceInfoHashMap.get(fromNum).isKing()));
        checkerPieceInfoHashMap.remove(fromNum);
    }



   /* Before jumping, it
    checks if the middle Block had a checker piece in it. If its not there it shouldn't jump.
    If there a piece in the middle block, then it'll jump over the block and removes the piece in the middle block */

    private void singleJump(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics graphics) {
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

        if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(checkerPieceInfoHashMap.get(midBlockNumber).getColor())) {
            System.out.println(checkerPieceInfoHashMap.get(fromNum).getColor() + " can't jump on " + checkerPieceInfoHashMap.get(midBlockNumber).getColor());
            return;
        }

        //remove the piece from middle block
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(((col + toCol) / 2) * dimension, ((row + toRow) / 2) * dimension, dimension, dimension);

        checkerPieceInfoHashMap.remove(midBlockNumber);

    }

    private boolean checkValidMoveForKing(Color pieceColor) {
        return !pieceColor.equals(stack.peek().getColor());
    }


    public boolean calculateDistance(CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock) {
        return (calculateDistance(fromBlock.getRow(), fromBlock.getCol(), toBlock.getRow(), toBlock.getCol()) > 1);
    }

    public int calculateDistance(int row, int col, int toRow, int toCol) {
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

        if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.red) && toRow == 0 || checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.black) && toRow == 7)
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
            checkerPieceInfoHashMap.put(midBlockNumber, new CheckerPieceInfo(midBlockNumber, color, false));
        }

        checkerPieceInfoHashMap.remove(previousToBlock.getBlockNumber());
        checkerPieceInfoHashMap.put(previousFromBlock.getBlockNumber(), previousFromBlock);


    }


}
