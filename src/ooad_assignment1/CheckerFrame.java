package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CheckerFrame extends JFrame {

    public CheckerFrame(){

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0,150,0));

        JPanel leftPanel = new JPanel();
        JTextField comments = new JTextField("");
        comments.setBackground(new Color(0,150,0));
        comments.setSize(300,50);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        SquarePanel sp = new SquarePanel();
        JPanel buttonsPanel = new JPanel();

        JButton previousBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");



//move in the input file is stored in movesList
        List<String> movesList = readMovesFromFile();
        List<Integer> currentMove = new ArrayList<>();


        nextBtn.addActionListener(e -> {
            if(currentMove.size() >= movesList.size()) {
                comments.setText("No more moves in the input file");
                System.out.println("No more moves in the input file");
                return;
            }
            Graphics g =sp.getGraphics();
            int fromMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[0]);
            int toMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[1]);

            sp.moveDiagonal(g,fromMove,toMove);
            currentMove.add(0);

        });


//on click of previous button it'll undo the move
        previousBtn.addActionListener(e -> {
            Graphics g = sp.getGraphics();
            if (currentMove.size()<=0) {
                comments.setText("No more moves to undo");
                System.out.println("No more moves to undo");
                return;
            }
            currentMove.remove(currentMove.size()-1);

            sp.undo(g);

        });


        leftPanel.add(sp);
        leftPanel.add(comments);

        buttonsPanel.setBackground(new Color(0,150,0));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
        buttonsPanel.add(previousBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0,100)));
        buttonsPanel.add(nextBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0,100)));
        buttonsPanel.add(comments);



        sp.setPreferredSize(new Dimension(480,480));


        mainPanel.add(leftPanel);
        mainPanel.add(buttonsPanel);



        setVisible(true);
        setSize(960,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        setResizable(false);
    }

    //reads the moves from the input file and stores it in an ArrayList
    private List<String> readMovesFromFile(){
        List<String> userMovesList = new ArrayList<>();
        File gameMoves = new File("/Users/saishree/IdeaProjects/CheckerBoardGame/src/Resources/CheckersMove.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(gameMoves));
            String line;
            while((line = br.readLine())!= null){
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