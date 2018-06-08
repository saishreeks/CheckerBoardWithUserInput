package ooad_assignment1;

import javafx.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;


public class BoardManager extends JPanel implements MouseListener {
    int x = 0, y = 0;
    int dimension = 60;
    int number = 0; //numbers the dark blocks
    boolean isDark = true; //decides to make a dark block/light block
    boolean evenRow = true; //to check for even row
    static HashMap<Integer, CheckerPieceInfo> checkerPieceInfoHashMap = new HashMap<>(); //stores the checkerPiece object with the block number
    static HashMap<Integer, CheckerBlocksInfo> blockInfoMap = new HashMap<>(); //stores the CheckerBlocks object with the block number
    Stack<CheckerPieceInfo> stack = new Stack<>(); //stores the moves in a stack
    Stack<Integer> jumpMadeStack = new Stack<>(); //stack to store the jumps
    ArrayList<Integer> validCanBeMovedBlock = new ArrayList<>(); // for normal moves
    List<Integer> validJumpsForFromBlock = new ArrayList<>(); // for jumps
    List<Integer> possibleToList; // gives the possible to moves to be highlighted

    HashMap<RowColumnMapping, Integer> rowColBlckNumHashMap = new HashMap<>();
    int clickedBlock=0;

    BoardManager() {
        addMouseListener(this);
    }


    /**
     * paint component creates the checker board with the initial placement of the checker pieces
     */
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

                        RowColumnMapping rowColumnMapping = new RowColumnMapping(i, j);
                        rowColBlckNumHashMap.put(rowColumnMapping, number);

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
                } else {
                    if (!isDark) {
                        number += 1;
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x, y, dimension, dimension);

                        RowColumnMapping rowColumnMapping = new RowColumnMapping(i, j);
                        rowColBlckNumHashMap.put(rowColumnMapping, number);

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

        /* not working ????????????????????????????????????????*/

//        this.gameInProgressHighlightFromBlocks(g);

    }

    protected void printFirst() {

        highLightBlock(21,Color.CYAN);
        highLightBlock(22,Color.CYAN);
        highLightBlock(23,Color.CYAN);
        highLightBlock(24,Color.CYAN);
    }


    /**
     * draws the checker pieces in the required blocks
     */
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

    /**
     * Before any move is made it checks for the jumps available for the current player
     */
    public List<Integer> checkForAvailableJumps() {
        List<Integer> validFromBlocksList = new ArrayList<>(); //Keeps track of checker pieces that has jumps.
        if (stack.size() > 0) {
            Color color = stack.peek().getColor(); //gives the color of the previous player
            Iterator it = checkerPieceInfoHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                /* If the previous player was red, it'll check for the available jumps for black and vice versa and
                 add it to the valid list*/
                if (checkerPieceInfoHashMap.get(pair.getKey()).getColor() != color) {
                    CheckerPieceInfo tempChecker = checkerPieceInfoHashMap.get(pair.getKey());
                    int tempBlockNumber = checkerPieceInfoHashMap.get(pair.getKey()).getBlockNumber();
                    if (checkForLegalJumps(tempChecker, blockInfoMap.get(tempBlockNumber)) > 0) { //changed
                        validFromBlocksList.add(tempBlockNumber);
                    }
                }
            }
        }
        return validFromBlocksList;
    }

    /**
     * checks for the legal jumps for the checker pieces in all the four diagonal directions
     */
    private int checkForLegalJumps(CheckerPieceInfo tempChecker, CheckerBlocksInfo checkerBlocksInfo) { // changed return type from boolean to int
        int row = checkerBlocksInfo.getRow();
        int col = checkerBlocksInfo.getCol();


        if (canJump(tempChecker.getColor(), row, col, row - 1, col + 1, row - 2, col + 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() - 7))
            return (tempChecker.getBlockNumber() - 7);

        if (canJump(tempChecker.getColor(), row, col, row - 1, col - 1, row - 2, col - 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() - 9))
            return (tempChecker.getBlockNumber() - 9);

        if (canJump(tempChecker.getColor(), row, col, row + 1, col + 1, row + 2, col + 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() + 9))
            return (tempChecker.getBlockNumber() + 9);

        if (canJump(tempChecker.getColor(), row, col, row + 1, col - 1, row + 2, col - 2, tempChecker.getBlockNumber(), tempChecker.getBlockNumber() + 7))
            return (tempChecker.getBlockNumber() + 7);

        return 0;
    }

    /**
     * This is called by the checkForLegalJumps method to check whether the player can legally jump from (r1,c1) to (r3,c3).
     * (r3,c3) is a position that is 2 rows and 2 columns distant from (r1,c1) and (r2,c2) is the square between (r1,c1) and (r3,c3).
     */
    private boolean canJump(Color color, int r1, int c1, int r2, int c2, int r3, int c3, int fromBlock, int toBlock) {

        int middleBlock = calculateMiddleBlockNumber(r1, c1, r3, c3, fromBlock, toBlock);
        if (color.equals(Color.red)) {
            if (checkerPieceInfoHashMap.get(fromBlock).isKing()) {
                if (r2 < 0 || r2 > 7 || r3 < 0 || r3 > 7 || c2 < 0 || c2 > 7 || c3 < 0 || c3 > 7) //(r3,c3) is off the board.
                    return false;
                if (!hasCheckerPiece(middleBlock)) //checks for the checker piece in the middle block
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red)) //???????
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red))
                    return false;
                if (hasCheckerPiece(toBlock)) //toBlock should be empty
                    return false;
            } else {
                if (r2 < 0 || r2 > 7 || r3 < 0 || r3 > 7 || c2 < 0 || c2 > 7 || c3 < 0 || c3 > 7)
                    return false;
                if (r3 > r1) //Regular red checker piece can only move  up.
                    return false;
                if (!hasCheckerPiece(middleBlock))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.red))
                    return false;
                if (hasCheckerPiece(toBlock))
                    return false;
            }

        }
        if (color.equals(Color.black)) {
            if (checkerPieceInfoHashMap.get(fromBlock).isKing()) {
                if (r2 < 0 || r2 > 7 || r3 < 0 || r3 > 7 || c2 < 0 || c2 > 7 || c3 < 0 || c3 > 7)
                    return false;
                if (!hasCheckerPiece(middleBlock))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                    return false;
                if (hasCheckerPiece(toBlock))
                    return false;
            } else {
                if (r2 < 0 || r2 > 7 || r3 < 0 || r3 > 7 || c2 < 0 || c2 > 7 || c3 < 0 || c3 > 7)
                    return false;
                if (r3 < r1) //Regular black checker piece can only move  down.
                    return false;
                if (!hasCheckerPiece(middleBlock))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                    return false;
                if (checkerPieceInfoHashMap.get(middleBlock).getColor().equals(Color.black))
                    return false;
                if (hasCheckerPiece(toBlock))
                    return false;
            }

        }
        return true;
    }

    /**
     * Given the from block and to block numebr in a jump scenario, it calculates the middle block
     */
    protected int calculateMiddleBlockNumber(int row, int col, int toRow, int toCol, int fromBlock, int toBlock) {
        int middleBlock = 0;
        if ((row % 2 != 0) && (col % 2 != 0) && (toRow % 2 != 0) && (toCol % 2 != 0))
            middleBlock = (fromBlock + toBlock) / 2 + 1;
        else
            middleBlock = (fromBlock + toBlock) / 2;
        return middleBlock;
    }

    /**
     * gives the checker piece present in the given block number
     */
    public CheckerPieceInfo getValueInPosition(int positionNumber) {

        if (checkerPieceInfoHashMap.containsKey(positionNumber))
            return checkerPieceInfoHashMap.get(positionNumber);

        return null;
    }


    public void moveDiagonal(Graphics g, int fromBlockNumber, int toBlockNumber) {
        moveDiagonal(g, blockInfoMap.get(fromBlockNumber), blockInfoMap.get(toBlockNumber), fromBlockNumber, toBlockNumber);
    }

    /**
     * After checking the available jumps, move diagonal is called. It checks for the distance between the From and To block
     * and decides whether it is a jump or not. In either case it checks for the valid moves and then update the hashmaps accordingly .
     * Also it pushes the moves i.e From block and To block information to the stack. This stack is used to implement the UNDo feature
     * and also it helps in alternating between the players
     */
    //Validates that the move-from block has a checker piece and move-to block doesn't and moves the checker piece
    public void moveDiagonal(Graphics graphics, CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock, int fromNum, int toNum) {

        CheckerPieceInfo fromCheckerPieceInfo;
        CheckerPieceInfo toCheckerPieceInfo;

        int row = fromBlock.getRow();
        int col = fromBlock.getCol();
        int toRow = toBlock.getRow();
        int toCol = toBlock.getCol();
        int fromX = fromBlock.getX();
        int fromY = fromBlock.getY();

        boolean testForCompleteJump = false; //in case of multiple jumps, it tracks for jump completion

        Color pieceColor = getValueInPosition(fromNum) != null ? getValueInPosition(fromNum).getColor() : null; //if there is no checkerpiece in the block return null

        /* when the distance between From and To block is 1, its a normal move*/
        if (calculateDistance(row, col, toRow, toCol) == 1) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }

            if (!isDiagonal(row, col, toRow, toCol)) {
                CheckerBoard.message.setText("It is not a diagonal. Not a valid move");
                System.out.println("It is not a diagonal");
                return;
            }
        }

        /* if the distance between to and from block is more than 1, then it is a jump.
         * If the distance is 2 then its a single jump. If the distance is more than 2, then it's a multiple jump.
         * Also if distance is more than 2 and if the distance is odd then jump is not possible
         * because there'll be even number of blocks in between from and to blocks*/

        else if (calculateDistance(row, col, toRow, toCol) == 2) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }
            if (!singleJump(fromNum, toNum, row, col, toRow, toCol, graphics)) return;
            testForCompleteJump = true;


        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 == 0) {
            if (!(validateConditions(fromNum, toNum, pieceColor))) {
                return;
            }

            fromNum = multipleJumps(fromNum, toNum, row, col, toRow, toCol, graphics);

        } else if (calculateDistance(row, col, toRow, toCol) > 2 && calculateDistance(row, col, toRow, toCol) % 2 != 0) {
            CheckerBoard.message.setText("Jump is not possible from " + fromNum + " to " + toNum);
            System.out.println("Jump is not possible from " + fromNum + " to " + toNum);
            return;
        }


        int toX = toBlock.getX();
        int toY = toBlock.getY();
        CheckerPieceInfo checkerPieceInfo = checkerPieceInfoHashMap.get(fromNum);
        CheckerPieceInfo toPieceInfo =  checkerPieceInfoHashMap.get(toNum);


/* draw the checker piece in the ToBlock. if it has reached the either sides of the board make it a King,
else just draw the piece in toBlock */
        if (isCrowned(fromNum, toRow)) {
            graphics.setColor(pieceColor);
            graphics.fillOval(toX + 10, toY + 10, 40, 40);
            graphics.setColor(Color.white);
            graphics.drawString("K", toX + 30, toY + 30);
            checkerPieceInfo.setKing(true);


        } else {
            drawCircle(graphics, toX, toY, pieceColor, checkerPieceInfoHashMap.get(fromNum).isKing());
        }


        /* update the hash map by adding the checker piece in the move-to location and deleting it from the move-from location */
        checkerPieceInfo.setBlockNumber(toNum);
        checkerPieceInfoHashMap.remove(fromNum);
        checkerPieceInfoHashMap.put(toNum, checkerPieceInfo);


        /*remove the checker piece from fromBlock by drawing a rectangle */
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(fromX, fromY, dimension, dimension);


        /* push the toBlock and fromBlock info to the stack */
        fromCheckerPieceInfo = new CheckerPieceInfo();
        fromCheckerPieceInfo.setBlockNumber(fromNum);
        fromCheckerPieceInfo.setColor(pieceColor);
        if (checkerPieceInfoHashMap.get(toNum).isKing())
        fromCheckerPieceInfo.setKing(true);
        stack.push(fromCheckerPieceInfo);

        toCheckerPieceInfo = new CheckerPieceInfo();
        toCheckerPieceInfo.setBlockNumber(toNum);
        toCheckerPieceInfo.setColor(pieceColor);
        if (checkerPieceInfoHashMap.get(toNum).isKing())
            toCheckerPieceInfo.setKing(true);
        stack.push(toCheckerPieceInfo);

        /* in case if there are still jumps available in the given position and  they are not completed, throws an error */
        if (testForCompleteJump && (checkForLegalJumps(checkerPieceInfoHashMap.get(toNum), blockInfoMap.get(toNum)) > 0)) {
            CheckerBoard.message.setText("Jump not complete");
            System.out.println("jump not complete");
            undo(getGraphics());
            testForCompleteJump = false;
        }
    }

    /**
     * for any given move, this validates the conditions like From Block should have a checker piece,
     * normal Red/Black can only move up/down, let sthe King move in all for directions, and the To block should be empty
     */
    private boolean validateConditions(int fromNum, int toNum, Color pieceColor) {
        if (!hasCheckerPiece(fromNum)) {
            CheckerBoard.message.setText("from " + fromNum + "has a no checker piece");
            System.out.println("from " + fromNum + "has a no checker piece");
            return false;
        }

        if (checkerPieceInfoHashMap.get(fromNum).isKing()) {
            if (!checkValidMoveForKing(pieceColor)) {
                CheckerBoard.message.setText("player should alternate");
                System.out.println("player should alternate");
                return false;
            }
        } else {

            if (!checkValidMove(fromNum, toNum, pieceColor)) {
                CheckerBoard.message.setText("Not a valid move");
                System.out.println("Not a valid move");
                return false;
            }
        }


        if (hasCheckerPiece(toNum)) {
            CheckerBoard.message.setText(toNum + " has piece. can't move");
            System.out.println("to: " + toNum + " has piece. can't move");
            return false;
        }

        /* Makes sure that RED plays first*/
        if (stack.empty() && !pieceColor.equals(Color.red)) {
            CheckerBoard.message.setText("Player with red checkers should make the first move");
            System.out.println("Player with red checkers should make the first move");
            return false;
        }
        return true;
    }

    /**
     * When the user input has distance more than two, multiple jumps is called. Multiple jumps are broken down into several single jumps.
     * After every jump it looks for the possible jumps from the current position
     */
    private int multipleJumps(int fromNum, int toNum, int row1, int col1, int toRow1, int toCol1, Graphics graphics) {

        int tempToNum = fromNum; // temporary To block after every jump

        int jumpCount = 0;
        /* Loops as long as there are possible jumps from given position */
        while (checkForLegalJumps(checkerPieceInfoHashMap.get(fromNum), blockInfoMap.get(fromNum)) > 0) {
            int row = blockInfoMap.get(tempToNum).getRow();
            int col = blockInfoMap.get(tempToNum).getCol();
            if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.red)) {
                /* King can jump in all four directions, whereas RED can only jump upwards*/
                if (checkerPieceInfoHashMap.get(fromNum).isKing()) {
                    if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col + 1, row + 2, col + 2, fromNum, fromNum + 9)) {
                        tempToNum = fromNum + 9;
                        makeTempJump(fromNum, tempToNum, graphics);
                        fromNum = tempToNum;
                        jumpCount++;
                        continue;
                    }
                    if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col - 1, row + 2, col - 2, fromNum, fromNum + 7)) {
                        tempToNum = fromNum + 7;
                        makeTempJump(fromNum, tempToNum, graphics);
                        fromNum = tempToNum;
                        jumpCount++;
                        continue;
                    }
                }
                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col + 1, row - 2, col + 2, fromNum, fromNum - 7)) {
                    tempToNum = fromNum - 7;
                    makeTempJump(fromNum, tempToNum, graphics);
                    fromNum = tempToNum; //to continue the jump from the current position make tempTo block as the From block
                    jumpCount++;
                    continue;
                }
                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col - 1, row - 2, col - 2, fromNum, fromNum - 9)) {
                    tempToNum = fromNum - 9;
                    makeTempJump(fromNum, tempToNum, graphics);
                    fromNum = tempToNum;
                    jumpCount++;
                    continue;
                }
            } else if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.black)) {

                /* King can jump in all four directions, whereas BLACK can only jump downwards*/
                if (checkerPieceInfoHashMap.get(fromNum).isKing()) {
                    if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col + 1, row - 2, col + 2, fromNum, fromNum - 7)) {
                        tempToNum = fromNum - 7;
                        makeTempJump(fromNum, tempToNum, graphics);
                        fromNum = tempToNum; //to continue the jump from the current position make tempTo block as the From block
                        jumpCount++;
                        continue;
                    }
                    if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row - 1, col - 1, row - 2, col - 2, fromNum, fromNum - 9)) {
                        tempToNum = fromNum - 9;
                        makeTempJump(fromNum, tempToNum, graphics);
                        fromNum = tempToNum;
                        jumpCount++;
                        continue;
                    }
                }
                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col + 1, row + 2, col + 2, fromNum, fromNum + 9)) {
                    tempToNum = fromNum + 9;
                    makeTempJump(fromNum, tempToNum, graphics);
                    fromNum = tempToNum;
                    jumpCount++;
                    continue;
                }

                if (canJump(checkerPieceInfoHashMap.get(fromNum).getColor(), row, col, row + 1, col - 1, row + 2, col - 2, fromNum, fromNum + 7)) {
                    tempToNum = fromNum + 7;
                    makeTempJump(fromNum, tempToNum, graphics);
                    fromNum = tempToNum;
                    jumpCount++;
                    continue;
                }
            }
        }
        /* once the checker reaches the final block where there aren't anymore jumps possible and if that is the To block given by the user,
        it is considered as a valid move */
        if (tempToNum == toNum) {
            checkerPieceInfoHashMap.put(toNum, new CheckerPieceInfo(toNum, checkerPieceInfoHashMap.get(fromNum).getColor(), checkerPieceInfoHashMap.get(fromNum).isKing()));
            return fromNum;
        } else {
            CheckerBoard.message.setText("Jump not possible. Invalid input");
//            System.out.println("Jump not possible. Invalid input");
//            jumpMadeStack.clear();
            return fromNum;
        }
    }

    /**
     * This is called by the multiple jumps to make single jumps and it updates the CheckerPiece hashmap
     */
    private void makeTempJump(int fromNum, int tempToNum, Graphics graphics) {
        if (!singleJump(fromNum, tempToNum, blockInfoMap.get(fromNum).getRow(), blockInfoMap.get(fromNum).getCol(), blockInfoMap.get(tempToNum).getRow(), blockInfoMap.get(tempToNum).getCol(), graphics))
            return;
        //push the temporary jump to the stack and update the other stack
        jumpMadeStack.push(tempToNum);
        checkerPieceInfoHashMap.put(tempToNum, new CheckerPieceInfo(tempToNum, checkerPieceInfoHashMap.get(fromNum).getColor(), checkerPieceInfoHashMap.get(fromNum).isKing()));
        checkerPieceInfoHashMap.remove(fromNum);
    }

    /**
     * This is called whenever there is a jump to be made. It first checks if the From and To are diagonal and
     * checks if the middle Block has a checker piece in it. If its not there it shouldn't jump.
     * If there a piece in the middle block, then it'll jump over the block and removes the piece in the middle block
     */

    private boolean singleJump(int fromNum, int toNum, int row, int col, int toRow, int toCol, Graphics graphics) {

        int midBlockNumber = 0;

        if ((row % 2 != 0) && (col % 2 != 0) && (toRow % 2 != 0) && (toCol % 2 != 0))
            midBlockNumber = (fromNum + toNum) / 2 + 1;
        else
            midBlockNumber = (fromNum + toNum) / 2;

        if (!isDiagonal(row, col, toRow, toCol)) {
            CheckerBoard.message.setText("It is not a diagonal. Jump is not possible");
            System.out.println("It is not a diagonal. Jump is not possible");
            return false;
        }

        if (!hasCheckerPiece(midBlockNumber)) {
            CheckerBoard.message.setText("There is no checker piece in " + midBlockNumber + "can't jump on an empty block");
            System.out.println("There is no checker piece in " + midBlockNumber + "can't jump on an empty block");
            return false;
        }
        /* Makes sure RED can't jump on Red and Black can't jump on black*/
        if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(checkerPieceInfoHashMap.get(midBlockNumber).getColor())) {
            CheckerBoard.message.setText("Invalid move");
            System.out.println(checkerPieceInfoHashMap.get(fromNum).getColor() + " can't jump on " + checkerPieceInfoHashMap.get(midBlockNumber).getColor());
            return false;
        }

        /*remove the piece from middle block */
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(((col + toCol) / 2) * dimension, ((row + toRow) / 2) * dimension, dimension, dimension);

        checkerPieceInfoHashMap.remove(midBlockNumber);
        return true;
    }

    /**
     * Checks ifthe move is valid for king. The check is that the player should alternate
     */
    private boolean checkValidMoveForKing(Color pieceColor) {
        return !pieceColor.equals(stack.peek().getColor());
    }

    /**
     * Returns true if distance between From and To block is > 1
     */
    public boolean calculateDistance(CheckerBlocksInfo fromBlock, CheckerBlocksInfo toBlock) {
        return (calculateDistance(fromBlock.getRow(), fromBlock.getCol(), toBlock.getRow(), toBlock.getCol()) > 1);
    }

    /**
     * Calculates the distance between From and To block
     */
    public int calculateDistance(int row, int col, int toRow, int toCol) {
        return Math.max(Math.abs(row - toRow), Math.abs(col - toCol));
    }

    /**
     * Red should move only up and black should move only down. And the player should alternate
     */
    private boolean checkValidMove(int fromNum, int toNum, Color pieceColor) {

        if (pieceColor.equals(Color.red))
            return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && fromNum > toNum : fromNum > toNum;

        return stack.size() > 0 ? !pieceColor.equals(stack.peek().getColor()) && toNum > fromNum : toNum > fromNum;
    }

    /**
     * checks if the move-from and move-to blocks are diagonal
     */
    private boolean isDiagonal(int row, int col, int toRow, int toCol) {
        return (Math.abs(row - toRow) == Math.abs(col - toCol));
    }

    /**
     * checks if the block has a checker piece or not
     */
    protected boolean hasCheckerPiece(int blockNumber) {
        CheckerPieceInfo val = getValueInPosition(blockNumber);
        if (val == null)
            return false;
        else
            return true;
    }

    /**
     * When the checker piece reaches the other side of the board it's crowned
     */
    private boolean isCrowned(int fromNum, int toRow) {

        if (checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.red) && toRow == 0 || checkerPieceInfoHashMap.get(fromNum).getColor().equals(Color.black) && toRow == 7)
            return true;
        return false;
    }

    /**
     * this is called when the user clicks on previous button. It pops the moves from the stack and
     * draws the Checker pieces in those blocks
     */
    public void undo(Graphics g) {
        Color color;
        if (stack.empty()) {
            CheckerBoard.message.setText("No valid moves to undo");
            System.out.println("No valid moves to undo");
            return;
        }
        CheckerPieceInfo previousToBlock = stack.pop(); // gives to
        CheckerPieceInfo previousFromBlock = stack.pop(); //gives from
        CheckerBlocksInfo from = blockInfoMap.get(previousFromBlock.getBlockNumber());
        drawCircle(g, from.getX(), from.getY(), previousToBlock.getColor(), previousToBlock.isKing());

        CheckerBlocksInfo to = blockInfoMap.get(previousToBlock.getBlockNumber());
        g.setColor(Color.DARK_GRAY);
        g.fillRect(to.getX(), to.getY(), dimension, dimension);

        /* If the previous move was a jump, it removes the checker piece from the To block and draws it in the middle and From blocks*/
        if (calculateDistance(from.getRow(), from.getCol(), to.getRow(), to.getCol()) != 1) {

            color = (previousFromBlock.getColor().equals(Color.red)) ? Color.black : Color.red;

            /* draw the the circle in the middle (jump over piece) */
            g.setColor(color);
            drawCircle(g, ((from.getCol() + to.getCol()) / 2) * dimension, ((from.getRow() + to.getRow()) / 2) * dimension, color, false);


            int midBlockNumber = 0;
            if ((from.getRow() % 2 != 0) && (from.getCol() % 2 != 0) && (to.getRow() % 2 != 0) && (to.getCol() % 2 != 0))
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2 + 1;
            else
                midBlockNumber = (previousFromBlock.getBlockNumber() + previousToBlock.getBlockNumber()) / 2;

            checkerPieceInfoHashMap.put(midBlockNumber, new CheckerPieceInfo(midBlockNumber, color, false));
        }

        /* update the hashmap by removing the piece from To block and add the From block checker piece*/

        checkerPieceInfoHashMap.remove(previousToBlock.getBlockNumber());
        checkerPieceInfoHashMap.put(previousFromBlock.getBlockNumber(), previousFromBlock);
    }

    /*Check for jumps first if its there then highlight and return, else highlight the other pieces */
    public void gameInProgressHighlightFromBlocks(Graphics g) {
        validCanBeMovedBlock = new ArrayList<>();


        if (stack.size() > 0) {
            Color color = stack.peek().getColor(); //gives the color of the previous player
            Iterator it = checkerPieceInfoHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                /* If the previous player was red, it'll check for the available jumps for black and vice versa and
                 add it to the valid list*/
                if (checkerPieceInfoHashMap.get(pair.getKey()).getColor() != color) {
                    CheckerPieceInfo tempChecker = checkerPieceInfoHashMap.get(pair.getKey());
                    int tempBlockNumber = checkerPieceInfoHashMap.get(pair.getKey()).getBlockNumber();
                    validJumpsForFromBlock = checkForAvailableJumps();
                    if (validJumpsForFromBlock.size() > 0) {
                        for (Integer i : validJumpsForFromBlock) {
                            highLightBlock(i, Color.cyan);
                        }
                        return;
                    }

                    if (checkForLegalMoves(tempBlockNumber)) { //changed
                        validCanBeMovedBlock.add(tempBlockNumber);

                    }
                }
            }
        } else {
            validCanBeMovedBlock.add(21);
            validCanBeMovedBlock.add(22);
            validCanBeMovedBlock.add(23);
            validCanBeMovedBlock.add(24);
        }
        for (Integer i : validCanBeMovedBlock) {
            highLightBlock(i, Color.cyan);
        }

        return;
    }


    private void highLightBlock(int tempBlockNumber, Color color) {

        if (tempBlockNumber !=0){ // not sure??????????

            CheckerBlocksInfo blocksInfo = blockInfoMap.get(tempBlockNumber);
            Graphics g = getGraphics();
            g.setColor(color);
            g.drawRect(blocksInfo.getX(), blocksInfo.getY(), dimension, dimension);
            g.drawRect(blocksInfo.getX() + 1, blocksInfo.getY() + 1, dimension - 2, dimension - 2);
        }
    }


    private void removeHighLightCanBeMovedBlocks(int tempBlockNumber) {

        if (tempBlockNumber !=0){  // not sure??????????????????

            CheckerBlocksInfo fromBlock = blockInfoMap.get(tempBlockNumber);
            Graphics g = getGraphics();
            g.setColor(Color.DARK_GRAY);
            g.drawRect(fromBlock.getX(), fromBlock.getY(), dimension, dimension);
            g.drawRect(fromBlock.getX() + 1, fromBlock.getY() + 1, dimension - 2, dimension - 2);
        }


    }

    private boolean checkForLegalMoves(int tempBlockNumber) {
        CheckerBlocksInfo tempBlock = blockInfoMap.get(tempBlockNumber);
        Color color = checkerPieceInfoHashMap.get(tempBlockNumber).getColor();
        int row = tempBlock.getRow();
        int col = tempBlock.getCol();

        if (checkerPieceInfoHashMap.get(tempBlockNumber).isKing()) {
            if (canMove(row, col, row - 1, col - 1))
                return true;
            if (canMove(row, col, row - 1, col + 1))
                return true;
            if (canMove(row, col, row + 1, col - 1))
                return true;
            if (canMove(row, col, row + 1, col + 1))
                return true;
        }

        if (color.equals(Color.red)) {
            if (canMove(row, col, row - 1, col - 1))
                return true;
            if (canMove(row, col, row - 1, col + 1))
                return true;
        }

        if (color.equals(Color.black)) {
            if (canMove(row, col, row + 1, col - 1))
                return true;
            if (canMove(row, col, row + 1, col + 1))
                return true;
        }

        return false;

    }

    private boolean canMove(int r1, int c1, int r2, int c2) {

        RowColumnMapping toBlockRowCol = new RowColumnMapping(r2, c2);
        if (r2 < 0 || r2 > 7 || c2 < 0 || c2 > 7) //(r2,c2) is off the board.
            return false;
        int toBlock = rowColBlckNumHashMap.get(toBlockRowCol);


        if (hasCheckerPiece(toBlock))
            return false;

        return true;
    }


    @Override
    public void mouseClicked(MouseEvent e) {


    }

    int fromMove = 0;
    int toMove = 0;

    @Override
    public void mousePressed(MouseEvent e) {


        CheckerBoard.message.setText("");
        int col = e.getX() / 60;
        int row = e.getY() / 60;
        RowColumnMapping obj = new RowColumnMapping(row, col);


        /* if user clicks on light colored blocks its an invalid move */
        if (!rowColBlckNumHashMap.containsKey(obj)) {
            removeHighLightCanBeMovedBlocks(fromMove);
            fromMove = 0;
            toMove = 0;
            CheckerBoard.message.setText("Invalid Move");
            System.out.println("invalid!!!!!!!!!");
            return;
        }
        if (fromMove > 0) {
            /* if the user changes the FROM piece and selects another FROM piece among the highlighted ones,
            then the newly selected piece is assigned to fromMove */
            if (hasCheckerPiece(rowColBlckNumHashMap.get(obj)) && (checkerPieceInfoHashMap.get(fromMove).getColor().equals(checkerPieceInfoHashMap.get(rowColBlckNumHashMap.get(obj)).getColor()))) {
                fromMove = rowColBlckNumHashMap.get(obj);
                /* the highlighted possible moves (green ) for previous selected piece is removed*/
                if (!possibleToList.isEmpty()) {
                    for (Integer i : possibleToList)
                        removeHighLightCanBeMovedBlocks(i);
                }
                clickBlock(fromMove);
                return;
            }
            toMove = rowColBlckNumHashMap.get(obj);
        } else {
            fromMove = rowColBlckNumHashMap.get(obj);
            if (!hasCheckerPiece(fromMove)) {
                System.out.println("There's no checker piece");
                CheckerBoard.message.setText("There's no checker piece");
                fromMove = 0;
                return;
            }

            if(stack.size()>0 && stack.peek().getColor().equals(checkerPieceInfoHashMap.get(fromMove).getColor())){
                CheckerBoard.message.setText("Opponent's turn");
                fromMove =0;
                return;
            }

            clickBlock(fromMove);

        }
        if ((fromMove > 0 && toMove > 0) && (fromMove != toMove)) {
            List<Integer> availableJumps = new ArrayList<>();
            availableJumps = checkForAvailableJumps();
            if (availableJumps.size() > 0) {
                if (availableJumps.contains(fromMove) && calculateDistance(blockInfoMap.get(fromMove), blockInfoMap.get(toMove))) {
                    moveDiagonal(getGraphics(), fromMove, toMove);
                    fromMove = 0;
                    toMove = 0;
                    availableJumps.clear();
                } else {
                    CheckerBoard.message.setText("There is a jump available");
                    System.out.println("There is a jump available");
                    fromMove = 0;
                    toMove = 0;
                    availableJumps.clear();
//                    return;
                }
            } else {
                moveDiagonal(getGraphics(), fromMove, toMove);
                fromMove = 0;
                toMove = 0;
                availableJumps.clear();
            }

            /* the highlighted possible moves (green ) is removed*/
            if (!possibleToList.isEmpty()) {
                for (Integer i : possibleToList)
                    removeHighLightCanBeMovedBlocks(i);
            }


            /** soon after a move is made, we have to highlight the next valid checkers that can be moved. first check for possible jumps
             * if jumps are present highlight only those blocks, else highlight the other blocks which can be moved*/

            if (!validJumpsForFromBlock.isEmpty()) {
                for (Integer i : validJumpsForFromBlock)
                    removeHighLightCanBeMovedBlocks(i);
            }


            if (!validCanBeMovedBlock.isEmpty()) { //???????????????
                for (Integer i : validCanBeMovedBlock)
                    removeHighLightCanBeMovedBlocks(i);
            }

            /* removes the white highlight*/ // not working ??????????????????


            gameInProgressHighlightFromBlocks(getGraphics());

        }

    }

    private void clickBlock(int fromMove) {
       if(clickedBlock > 0) removeHighLightCanBeMovedBlocks(clickedBlock);
        clickedBlock=fromMove;
        highLightBlock(fromMove,Color.white);

        highLightPossibleToBlocks(fromMove); // green Highlight

    }

    private void addPossibleToBlockToList(RowColumnMapping possible1) {
        int possibleToBlockNumber = rowColBlckNumHashMap.get(possible1);
        if (!hasCheckerPiece(possibleToBlockNumber)){
            highLightBlock(possibleToBlockNumber, Color.green);
            possibleToList.add(possibleToBlockNumber);
        }
    }



    private void highLightPossibleToBlocks(int fromMove) {

        possibleToList = new ArrayList<>();

        if (checkForAvailableJumps().size() > 0) {
            int possibleTo = checkForLegalJumps(checkerPieceInfoHashMap.get(fromMove), blockInfoMap.get(fromMove));
            possibleToList.add(possibleTo);
            highLightBlock(possibleTo, Color.green);
        } else {
            int row = blockInfoMap.get(fromMove).getRow();
            int col = blockInfoMap.get(fromMove).getCol();

            if (checkerPieceInfoHashMap.get(fromMove).isKing()){
                if ((row-1)>0 && (col-1)>0){
                    RowColumnMapping possible1 = new RowColumnMapping(row - 1, col - 1);
                    addPossibleToBlockToList(possible1);
                }

               if ((row-1)>0 && (col+1)<8){
                   RowColumnMapping possible2 = new RowColumnMapping(row - 1, col + 1);
                   addPossibleToBlockToList(possible2);
               }

               if ((row+1)<8 && (col-1)>0){
                   RowColumnMapping possible3 = new RowColumnMapping(row + 1, col - 1);
                   addPossibleToBlockToList(possible3);
               }

               if ((row+1)<8 && (col+1)<8){
                   RowColumnMapping possible4 = new RowColumnMapping(row + 1, col + 1);
                   addPossibleToBlockToList(possible4);
               }

                return;
            }


            if (checkerPieceInfoHashMap.get(fromMove).getColor().equals(Color.red)) {

                if (row-1 < 0)
                    return;

                if ((col-1) >= 0 && (col+1) <8){
                    RowColumnMapping possible1 = new RowColumnMapping(row - 1, col - 1);
                    RowColumnMapping possible2 = new RowColumnMapping(row - 1, col + 1);
                    addPossibleToBlockToList(possible1);
                    addPossibleToBlockToList(possible2);
                    return;
                }

                if ((col - 1) <0){
                    RowColumnMapping possible2 = new RowColumnMapping(row - 1, col + 1);
                    addPossibleToBlockToList(possible2);

                }

                if ((col + 1) >7){
                    RowColumnMapping possible1 = new RowColumnMapping(row - 1, col - 1);
                    addPossibleToBlockToList(possible1);
                }
            }
            if (checkerPieceInfoHashMap.get(fromMove).getColor().equals(Color.BLACK)) {

                if (row+1 > 7)
                    return;

                if ((col-1) >= 0 && (col+1) <8){
                    RowColumnMapping possible1 = new RowColumnMapping(row + 1, col - 1);
                    RowColumnMapping possible2 = new RowColumnMapping(row + 1, col + 1);
                    addPossibleToBlockToList(possible1);
                    addPossibleToBlockToList(possible2);
                    return;
                }

                if ((col - 1) <0){
                    RowColumnMapping possible2 = new RowColumnMapping(row + 1, col + 1);
                    addPossibleToBlockToList(possible2);
                }

                if ((col + 1) >7){
                    RowColumnMapping possible1 = new RowColumnMapping(row + 1, col - 1);
                    addPossibleToBlockToList(possible1);
                }
            }

        }


    }



    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
