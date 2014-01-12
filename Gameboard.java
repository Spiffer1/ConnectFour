import java.awt.*;

public class Gameboard
{
    private Circle[][] holes = new Circle[6][7];
    private int diameter = 40;
    private int spacing = 8;
    private int gridX = 100;    // location of the upper left corner of the gameboard
    private int gridY = 100;
    private int gridWidth, gridHeight;

    public Gameboard(Color backColor)
    {    
        gridWidth = 7 * diameter + 8 * spacing;
        gridHeight = 6 * diameter + 7 * spacing;
        for (int row = 0; row < 6; row++)
        {
            for (int col = 0; col < 7; col++)
            {
                int x = gridX + (col + 1) * spacing + (int)((col + .5) * diameter);
                int y = gridY + gridHeight - (row + 1) * spacing - (int)((row + .5) * diameter);
                holes[row][col] = new Circle( x, y, diameter, backColor);
            }
        }
    }

    public void draw(Graphics g)
    {
        Color oldColor = g.getColor();
        g.setColor(Color.blue);
        g.fillRect(gridX, gridY, gridWidth, gridHeight);
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                holes[i][j].draw(g);
            }
        }
        g.setColor(oldColor);
    }
    public int getGridWidth()
    {
        return gridWidth;
    }
    public int getGridHeight()
    {
        return gridHeight;
    }
    public int getGridX()
    {
        return gridX;
    }
    public int getGridY()
    {
        return gridY;
    }
    public int getDiameter()
    {
        return diameter;
    }
    public int getSpacing()
    {
        return spacing;
    }
    public int getColumn(int mouseX)
    {
        int x = mouseX - gridX - spacing / 2;
        int column = x / (spacing + diameter);
        column = Math.max(column, 0);   // restrict column values to a range of 0 - 6
        column = Math.min(column, 6);
        return column;
    }
    public int getXFromCol(int col)
    {
        return gridX + col * (spacing + diameter);
    }
    public void setHoleColor(int row, int col, Color color)
    {
        holes[row][col].setColor(color);
    }
}
