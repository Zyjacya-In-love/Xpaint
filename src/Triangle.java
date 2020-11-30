import java.awt.*;


public class Triangle extends Shape {

    private int[] x, y;
    private int n = 0;

    public Triangle()
    {
        super();
    }

    public Triangle(int x1, int y1, int x2, int y2, Color color, int thickness) {
        super(x1, y1, x2, y2, color, thickness);
    }
    public Triangle(int[] x, int[] y, int n, Color color, int thickness) {
        this.x = x;
        this.y = y;
        this.n = n;
        this.color = color;
        this.thickness = thickness;
    }

    public void setXYn(int[] x, int[] y, int n) {
        this.x = x;
        this.y = y;
        this.n = n;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color); //sets the color
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(thickness));
        g.drawPolyline(x, y, n); //draws the line
    }
}