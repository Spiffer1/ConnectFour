import javax.swing.*;
import java.awt.*;
public class ConnectFourApp
{
    public static void main(String[] args)
    {
        BoardModel board = new BoardModel();
        AIPlayer computer = new AIPlayer();
        startGUI(board, computer);
    }

    public static void startGUI(BoardModel board, AIPlayer computer)
    {
        JFrame theGUI = new JFrame();
        theGUI.setSize(600,500);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        BoardPanel panel = new BoardPanel(board, computer);
        Container pane = theGUI.getContentPane();
        pane.add(panel);
        theGUI.setVisible(true);
    }
}
