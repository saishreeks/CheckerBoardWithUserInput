package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The constructor creates the Checker Board, creates and manages
 * the buttons and message label, adds all the components, and sets
 * the bounds of the components.  A null layout is used.
 */

public class CheckerBoard extends JFrame implements ActionListener {

   public static JLabel message = new JLabel("", JLabel.CENTER);
   private JButton previousBtn = new JButton("Previous");
   private JButton nextBtn = new JButton("Next");
   BoardManager boardController = new BoardManager();

    List<Integer> currentMove = new ArrayList<>();



    public CheckerBoard() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);  // I will do the layout myself.
        mainPanel.setPreferredSize(new Dimension(800, 600));

        mainPanel.setBackground(new Color(0, 150, 0));  // Dark green background.
        mainPanel.add(boardController);
        mainPanel.add(previousBtn);
        mainPanel.add(nextBtn);
        mainPanel.add(message);

        /* Set the position and size of each component by calling
       its setBounds() method. */

        boardController.setBorder(BorderFactory.createLineBorder(Color.black));
        boardController.setBounds(20, 20, 480, 480);
        previousBtn.setBounds(580, 60, 120, 30);
        nextBtn.setBounds(580, 120, 120, 30);
        message.setBounds(20, 520, 500, 30);

        nextBtn.addActionListener(this);
        previousBtn.addActionListener(this);
        message.setFont(new Font("Serif", Font.BOLD, 16));

        setVisible(true);
        setSize(800, 600);
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        setResizable(false);

    }

    /**
     * Respond to user's click on one of the two buttons.
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == nextBtn)
            doNextMove();
        else if (src == previousBtn)
            doPreviousMove();
    }

    /** performs the previous move */
    private void doPreviousMove() {
        Graphics g = boardController.getGraphics();
        if (currentMove.size() <= 0) {
            message.setText("No more moves in the input file to undo");
            System.out.println("No more moves in the input file to undo");
            return;
        }
        currentMove.remove(currentMove.size() - 1);
        boardController.undo(g);
    }

    /** performs the next move */
    private void doNextMove() {
        //moves in the input file is stored in movesList
        List<String> movesList = readMovesFromFile();
        message.setText("");

        if (currentMove.size() >= movesList.size()) {
            message.setText("No more moves in the input file");
            System.out.println("No more moves in the input file");
            return;
        }
        Graphics g = boardController.getGraphics();


        int fromMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[0]);
        int toMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[1]);
        List<Integer> availableJumps = new ArrayList<>();
        availableJumps = boardController.checkForAvailableJumps();
        if (toMove > 0 && toMove < 33 && fromMove > 0 && fromMove < 33) {
            /* before making the next move, it checks for the available jumps for the current player. If there aren't any jumps
            for the player any piece can be moved */
            if (availableJumps.size() > 0) {

                /* If the player has picked one of the pieces that has a jump then proceed, else display a message */
                if (availableJumps.contains(fromMove) && boardController.calculateDistance(boardController.blockInfoMap.get(fromMove), boardController.blockInfoMap.get(toMove))) {
                    boardController.moveDiagonal(g, fromMove, toMove);
                    currentMove.add(0);
                    availableJumps = null;
                } else {
                    message.setText("There is a jump available");
                    System.out.println("There is a jump available");
                    currentMove.add(0);
                    availableJumps = null;
                }
            } else {
                boardController.moveDiagonal(g, fromMove, toMove);
                currentMove.add(0);
                availableJumps = null;
            }
        } else {
            message.setText("The input value is not valid");
            System.out.println(" The input value is not valid");
        }

    }


    /** reads the moves from the input file and stores it in an ArrayList */
    private List<String> readMovesFromFile() {
        List<String> userMovesList = new ArrayList<>();
        File gameMoves = new File("/Users/saishree/IdeaProjects/CheckerBoardGame/src/Resources/CheckersMove.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(gameMoves));
            String line;
            while ((line = br.readLine()) != null) {
                line.trim();
                if(line.isEmpty()) continue;
                userMovesList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMovesList;
    }


}
