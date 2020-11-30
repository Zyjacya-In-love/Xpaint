import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    protected int x1,y1,x2,y2; // coordinates of shape
    protected int thickness; // thickness of shape
    protected Color color; // color of shape
    public boolean isPencilEraserStart = false;
    public boolean isPencilEraserfinish = false;


    public Shape() {
        x1=0;
        y1=0;
        x2=0;
        y2=0;
        thickness = 1;
        color=Color.BLACK;
    }

    public Shape(int x1, int y1, int x2, int y2, Color color, int thickness) {
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.color=color;
        this.thickness=thickness;
    }

    public void setX1(int x1)
    {
        this.x1=x1;
    }

    public void setY1(int y1)
    {
        this.y1=y1;
    }

    public void setX2(int x2)
    {
        this.x2=x2;
    }

    public void setY2(int y2)
    {
        this.y2=y2;
    }

    public void setColor(Color color)
    {
        this.color=color;
    }

    public void setThickness(int width) {
        this.thickness = thickness;
    }

    public int getUpperLeftX()
    {
        return Math.min(x1,x2);
    }
    
    public int getUpperLeftY()
    {
        return Math.min(y1,y2);
    }

    public int getWidth()
    {
        return Math.abs(x1-x2);
    }

    public int getHeight()
    {
        return Math.abs(y1-y2);
    }

    abstract public void draw(Graphics g );

}
