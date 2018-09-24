package ooad_assignment1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
/**
 * The constructor creates the Checker Board, creates and manages
 * the buttons and message label, adds all the components, and sets
 * the bounds of the components.  A null layout is used.
 */

public class CheckerBoard extends JFrame implements ActionListener {

   public static JLabel message = new JLabel("", JLabel.CENTER);
    private JButton startGameButtin = new JButton("START GAME");


    BoardManager boardController = new BoardManager();

    List<Integer> currentMove = new ArrayList<>();



    public CheckerBoard() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);  // I will do the layout myself.
        mainPanel.setPreferredSize(new Dimension(800, 600));

        mainPanel.setBackground(new Color(0, 150, 0));  // Dark green background.
        mainPanel.add(boardController);
        mainPanel.add(startGameButtin);


        mainPanel.add(message);

        /* Set the position and size of each component by calling
       its setBounds() method. */

        boardController.setBorder(BorderFactory.createLineBorder(Color.black));
        boardController.setBounds(20, 20, 480, 480);
        startGameButtin.setBounds(580, 60, 120, 30);


        message.setBounds(20, 520, 500, 30);
        startGameButtin.addActionListener(this );

        message.setFont(new Font("Serif", Font.BOLD, 16));

        setVisible(true);
        setSize(800, 600);
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        setResizable(false);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        boardController.printFirst();
        startGameButtin.setVisible(false);
        boardController.enabled=true;
        //        this.disable();
    }
}
