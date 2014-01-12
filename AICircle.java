/*
 * This version of Circle is for the computer-player's Top Circle. It allows that circle
 * to animate when the computer selects a column to play in. Animation occurs via the
 * move() method. The calling method (BoardPanel) sets the targetColumn it wants the
 * circle to move to and starts a Timer that calls AICircle's move method.
 */
import java.awt.*;
public class AICircle extends Circle
{
    private int speed;
    private int direction;
    private int targetColumn;
    
    public AICircle(int xPos, int yPos, int diam, Color color_)
    {
        super(xPos, yPos, diam, color_);
        speed = 5;
        direction = 1;
        targetColumn = 0;
    }
    public void move()
    {
        x += speed * direction;
    }
    public void setTargetColumn(int col)
    {
        targetColumn = col;
    }
    public int getTargetColumn()
    {
        return targetColumn;
    }
}
