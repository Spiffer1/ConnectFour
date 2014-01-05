import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel
{
    private Circle topCircle;
    private Gameboard grid;
    private int mouseX, mouseY;
    private BoardModel board;
    private AIPlayer computer;
    boolean gameWon;

    public BoardPanel(BoardModel board_, AIPlayer computer_)
    {
        board = board_;
        computer = computer_;
        gameWon = false;
        Color lightBlue = new Color(0, 240, 255);
        setBackground(lightBlue);
        grid = new Gameboard(lightBlue);
        mouseX = grid.getGridX() + grid.getSpacing() + grid.getDiameter() / 2;
        topCircle = new Circle(mouseX, 70, grid.getDiameter(), Color.red); // player 1 is red; player 2 is yellow
        PanelListener myListener = new PanelListener();
        addMouseMotionListener(myListener);
        addMouseListener(myListener);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        topCircle.setX(mouseX);
        topCircle.draw(g);
        grid.draw(g);
    }
    private class PanelListener extends MouseInputAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            if (!gameWon && board.getWhoseTurn() == 2)
            {
                int circleX = topCircle.getX();
                int column = grid.getColumn(circleX);
                int correspondingRow = board.pickColumn(column);

                grid.setHoleColor(correspondingRow, column, Color.red);
                topCircle.setColor(Color.yellow);

            }
            repaint();
            int winner = board.findWinner();
            if (winner == 2)
            {
                gameWon = true;
                System.out.println("Game Over! Human wins!");
            }
            // Human's turn is done; continue with computer's turn
            if (!gameWon)
            {
                int column = computer.chooseMove(board);
                System.out.println("Computer chooses: " + column);
                int row = board.pickColumn(column);
                grid.setHoleColor(row, column, Color.yellow);
                topCircle.setColor(Color.red);
                repaint();
                winner = board.findWinner();
                if (winner == 1)
                {
                    gameWon = true;
                    System.out.println("Game Over! Computer wins!");
                }
            }
        }

        public void mouseMoved(MouseEvent e)
        {
            if (!gameWon && board.getWhoseTurn() == 2)
            {
                mouseX = e.getX();
                mouseY = e.getY();
                if (mouseX > grid.getGridX() + grid.getDiameter()/2 && mouseX < grid.getGridX() + grid.getGridWidth() - grid.getDiameter()/2
                && mouseY > grid.getGridY() && mouseY < grid.getGridY() + grid.getGridHeight() )
                {
                    repaint();
                }
            }
        }
    }
}

