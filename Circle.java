import java.awt.*;

public class Circle
{
    private int x, y, diameter;
    private Color color;
    
    public Circle(int xPos, int yPos, int diam, Color color_)
    {
        x = xPos;
        y = yPos;
        diameter = diam;
        color = color_;
    }
    public void draw(Graphics g)
    {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
        g.setColor(oldColor);
    }
    public void move(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }
    public void setColor(Color color_)
    {
        color = color_;
    }
    public void setX(int xPos)
    {
        x = xPos;
    }
    public int getX()
    {
        return x;
    }
}
