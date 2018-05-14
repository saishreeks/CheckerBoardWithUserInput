package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CheckerFrame extends JFrame implements ActionListener {

   public static JLabel message = new JLabel("", JLabel.CENTER);
   private JButton previousBtn = new JButton("Previous");
   private JButton nextBtn = new JButton("Next");
   BoardController boardController = new BoardController();

    List<Integer> currentMove = new ArrayList<>();



    public CheckerFrame() {

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
        message.setBounds(20, 520, 350, 30);

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


    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == nextBtn)
            doNextMove();
        else if (src == previousBtn)
            doUndo();
    }

    private void doUndo() {
        Graphics g = boardController.getGraphics();
        if (currentMove.size() <= 0) {
            message.setText("No more moves in the input file to undo");
            System.out.println("No more moves in the input file to undo");
            return;
        }
        currentMove.remove(currentMove.size() - 1);
        boardController.undo(g);
    }

    private void doNextMove() {
        //moves in the input file is stored in movesList
        List<String> movesList = readMovesFromFile();


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
            if (availableJumps.size() > 0) {

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


    //reads the moves from the input file and stores it in an ArrayList
    private List<String> readMovesFromFile() {
        List<String> userMovesList = new ArrayList<>();
        File gameMoves = new File("/Users/saishree/IdeaProjects/CheckerBoardGame/src/Resources/CheckersMove.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(gameMoves));
            String line;
            while ((line = br.readLine()) != null) {
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
