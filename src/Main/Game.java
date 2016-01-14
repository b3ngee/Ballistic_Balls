package Main;

import javax.swing.*;

/**
 * Created by BenGee on 2015-09-10.
 */
public class Game {

    public static void main(String[] args) {

        JFrame window = new JFrame("Ballistic Beast");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setContentPane(new GamePanel());
        window.pack();
        window.setVisible(true);

    }
}
