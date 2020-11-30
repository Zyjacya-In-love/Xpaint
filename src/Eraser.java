import java.awt.*;


public class Eraser extends Shape {

    public Eraser()
    {
        super();
    }

    public Eraser(int x1, int y1, int x2, int y2, Color color, int thickness)
    {
        super(x1, y1, x2, y2, color, thickness);
    } 

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE); //sets the color
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(thickness));
        g.drawLine(x1, y1, x2, y2); //draws the line
    }
}