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

        SquarePanel sp = new SquarePanel();
        JPanel jp = new JPanel();

        JButton previousBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");


        List<String> movesList = readMovesFromFile();
        List<Integer> currentMove = new ArrayList<>();

        nextBtn.addActionListener(e -> {
            if(currentMove.size() >= movesList.size()) {
                System.out.println("No more moves left");
                return;
            }
            Graphics g =sp.getGraphics();
            int fromMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[0]);
            int toMove = Integer.parseInt(movesList.get(currentMove.size()).split("-")[1]);

            sp.moveDiagonal(g,fromMove,toMove);
            currentMove.add(0);

        });

        previousBtn.addActionListener(e -> {
            Graphics g = sp.getGraphics();
            currentMove.remove(currentMove.size()-1);
            sp.undo(g);
        });


        jp.setBackground(new Color(0,150,0));
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        jp.add(previousBtn);
        jp.add(Box.createRigidArea(new Dimension(0,100)));
        jp.add(nextBtn);



        sp.setPreferredSize(new Dimension(480,480));

        mainPanel.add(sp);
        mainPanel.add(jp);

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

        for (String line:userMovesList){
            System.out.println(line);
        }
        return userMovesList;
    }



}
