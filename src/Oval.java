import java.awt.*;


public class Oval extends Shape {

    public Oval()
    {
        super();
    }

    public Oval(int x1, int y1, int x2, int y2, Color color, int thickness)
    {
        super(x1, y1, x2, y2, color, thickness);
    }

    @Override
    public void draw( Graphics g ) {
        g.setColor(color); //sets the color
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(thickness));
        g.drawOval(getUpperLeftX(), getUpperLeftY(), getWidth(), getHeight()); //draws a regular oval
    }
}