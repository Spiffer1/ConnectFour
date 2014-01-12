import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel
{
    private Circle topCircle;
    private AICircle aiTopCircle;
    private Gameboard grid;
    private int mouseX, mouseY;
    private BoardModel board;
    private AIPlayer computer;
    boolean gameWon;
    javax.swing.Timer timer;

    public BoardPanel(BoardModel board_, AIPlayer computer_)
    {
        board = board_;
        computer = computer_;
        gameWon = false;
        Color lightBlue = new Color(0, 240, 255);
        setBackground(lightBlue);
        grid = new Gameboard(lightBlue);
        mouseX = grid.getGridX() + grid.getSpacing() + grid.getDiameter() / 2;
        topCircle = new Circle(mouseX, grid.getGridY() - grid.getDiameter() / 2 - grid.getSpacing(), grid.getDiameter(), Color.red); // player 1 is red; player 2 is yellow
        aiTopCircle = new AICircle(mouseX, grid.getGridY() - grid.getDiameter() / 2 - grid.getSpacing(), grid.getDiameter(), Color.yellow);
        PanelListener myListener = new PanelListener();
        addMouseMotionListener(myListener);
        addMouseListener(myListener);
        timer = new javax.swing.Timer(33, new MoveListener());
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (board.getWhoseTurn() == 1)
        {
            aiTopCircle.draw(g);
        }
        else
        {
            topCircle.setX(mouseX);
            topCircle.draw(g);
        }
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

                repaint();
                if (board.findWinner() == 2)
                {
                    gameWon = true;
                    System.out.println("Game Over! Human wins!");
                }
                board.switchTurn();
            }
            // Human's turn is done; continue with computer's turn
            if (!gameWon)
            {
                int column = computer.chooseMove(board);
                System.out.println("Computer chooses: " + column);
                aiTopCircle.setX(grid.getGridX() + grid.getSpacing() + grid.getDiameter() / 2);
                aiTopCircle.setTargetColumn(column);
                timer.start();  // This starts animation of moving aiTopCircle. When the aiTopCircle reaches
                // target column, the Timer is stopped, pickColumn() is called, and switchTurn() is called.
                if (board.findWinner() == 1)
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
    public class MoveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            aiTopCircle.move();
            int x = aiTopCircle.getX();
            int column = aiTopCircle.getTargetColumn();
            if ( x >= grid.getXFromCol(column) )
            {
                timer.stop();              
                int row = board.pickColumn(column);
                grid.setHoleColor(row, column, Color.yellow);
                board.switchTurn();
            }
            repaint();
        }
    }
}

