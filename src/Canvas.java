import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.RenderingHints;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionAdapter;
//import java.awt.event.MouseMotionListener;
//import java.awt.image.BufferedImage;
//import java.awt.image.RenderedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.LinkedList;

public class Canvas extends JPanel {
//	private int X1, Y1, X2, Y2;
	private Graphics2D g;
	private String currentShapeType;
	private Shape currentShapeObject; //stores the current shape object
	private Color currentShapeColor; //current shape color
	private LinkedList<Shape> exShapes; //dynamic stack of shapes
//	private LinkedList<MyShape> clearedShapes; //dynamic stack of cleared shapes from undo
//	private Image img, background, undoTemp, redoTemp;
//	ArrayList<Shape> shapes = new ArrayList<Shape>();
//	private final SizedStack<Image> undoStack = new SizedStack<>(12);
//	private final SizedStack<Image> redoStack = new SizedStack<>(12);
//	private Rectangle shape;
//	private Point startPoint;
//	private MouseMotionListener motion;
//	private MouseListener listener;

//	public void save(File file) {
//		try {
//			ImageIO.write((RenderedImage) img, "PNG", file);
//		} catch (IOException ex) {
//		}
//	}
//
//	public void load(File file) {
//		try {
//			img = ImageIO.read(file);
//			g = (Graphics2D) img.getGraphics();
//		}
//
//		catch (IOException ex) {
//		}
//	}
//
//	protected void paintComponent(Graphics g1) {
//		if (img == null) {
//			img = createImage(getSize().width, getSize().height);
//			g = (Graphics2D) img.getGraphics();
//			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);
//
//			clear();
//
//		}
//		g1.drawImage(img, 0, 0, null);
//
//		if (shape != null) {
//			Graphics2D g2d = (Graphics2D) g;
//			g2d.draw(shape);
//		}
//	}

	public Canvas() {
		setBackground(Color.WHITE);
		exShapes = new LinkedList<Shape>(); //initialize myShapes dynamic stack

		currentShapeObject=null;
		currentShapeColor=Color.BLACK;
		MouseHandler handler = new MouseHandler();
		addMouseListener( handler );
		addMouseMotionListener( handler );
//		defaultListener();
	}
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		// draw the shapes
		Shape[] shapeArray=exShapes.toArray(new Shape[exShapes.size()]);
		for ( int counter=shapeArray.length-1; counter>=0; counter-- )
			shapeArray[counter].draw(g);

		//draws the current Shape Object if it is not null
		if (currentShapeObject!=null)
			currentShapeObject.draw(g);
	}
	private class MouseHandler extends MouseAdapter {
		public void mousePressed( MouseEvent event ) {
			if("line".equals(currentShapeType)) {
				currentShapeObject= new Line( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor);
			}

		}

		/**
		 * When mouse is released set currentShapeObject's x2 & y2 to mouse pos.
		 * Then addFront currentShapeObject onto the myShapes dynamic Stack
		 * and set currentShapeObject to null [clearing current shape object since it has been drawn].
		 * Lastly, it clears all shape objects in clearedShapes [because you cannot redo after a new drawing]
		 * and calls repaint() to redraw panel.
		 */
		public void mouseReleased( MouseEvent event )
		{
			//sets currentShapeObject x2 & Y2
			currentShapeObject.setX2(event.getX());
			currentShapeObject.setY2(event.getY());

			exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes

			currentShapeObject=null; //sets currentShapeObject to null
//			clearedShapes.makeEmpty(); //clears clearedShapes
			repaint();

		} // end method mouseReleased

		/**
		 * This method gets the mouse pos when it is moving and sets it to statusLabel.
		 */
		public void mouseMoved( MouseEvent event )
		{
//			statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));
		} // end method mouseMoved

		/**
		 * This method gets the mouse position when it is dragging and sets x2 & y2 of current shape to the mouse pos
		 * It also gets the mouse position when it is dragging and sets it to statusLabel
		 * Then it calls repaint() to redraw the panel
		 */
		public void mouseDragged( MouseEvent event )
		{
			//sets currentShapeObject x2 & Y2
			currentShapeObject.setX2(event.getX());
			currentShapeObject.setY2(event.getY());

			//sets statusLabel to current mouse position
//			statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));

			repaint();

		} // end method mouseDragged

	}// end MouseHandler
	//
//	public void defaultListener() {
//		setDoubleBuffered(false);
//		listener = new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				saveToStack(img);
//				X2 = e.getX();
//				Y2 = e.getY();
//			}
//		};
//
//		motion = new MouseMotionAdapter() {
//			public void mouseDragged(MouseEvent e) {
//				X1 = e.getX();
//				Y1 = e.getY();
//
//				if (g != null) {
//					g.drawLine(X2, Y2, X1, Y1);
//					repaint();
//					X2 = X1;
//					Y2 = Y1;
//				}
//			}
//		};
//		addMouseListener(listener);
//		addMouseMotionListener(motion);
//	}
//
//	public void addRectangle(Rectangle rectangle, Color color) {
//
//		Graphics2D g2d = (Graphics2D) img.getGraphics();
//		g2d.setColor(color);
//		g2d.draw(rectangle);
//		repaint();
//	}
//
//	public void red() {
//		g.setPaint(Color.red);
//	}
//
//	public void black() {
//		g.setPaint(Color.black);
//	}
//
//	public void magenta() {
//		g.setPaint(Color.magenta);
//	}
//
//	public void green() {
//		g.setPaint(Color.green);
//	}
//
//	public void blue() {
//		g.setPaint(Color.blue);
//	}
//
//	public void gray() {
//		g.setPaint(Color.GRAY);
//	}
//
//	public void orange() {
//		g.setPaint(Color.ORANGE);
//	}
//
//	public void yellow() {
//		g.setPaint(Color.YELLOW);
//	}
//
//	public void pink() {
//		g.setPaint(Color.PINK);
//	}
//
//	public void cyan() {
//		g.setPaint(Color.CYAN);
//	}
//
//	public void lightGray() {
//		g.setPaint(Color.lightGray);
//	}
//
//	public void picker(Color color) {
//		g.setPaint(color);
//	}
//
//	public void clear() {
//		if (background != null) {
//			setImage(copyImage(background));
//		} else {
//			g.setPaint(Color.white);
//			g.fillRect(0, 0, getSize().width, getSize().height);
//			g.setPaint(Color.black);
//		}
//		repaint();
//	}
//
//	public void undo() {
//		if (undoStack.size() > 0) {
//			undoTemp = undoStack.pop();
//			redoStack.push(img);
//			setImage(undoTemp);
//		}
//	}
//
//	public void redo() {
//		if (redoStack.size() > 0) {
//			redoTemp = redoStack.pop();
//			undoStack.push(img);
//			setImage(redoTemp);
//		}
//	}
//
//	public void pencil() {
//		removeMouseListener(listener);
//		removeMouseMotionListener(motion);
//		defaultListener();
//
//	}
//
//	public void rect() {
//		removeMouseListener(listener);
//		removeMouseMotionListener(motion);
//		MyMouseListener ml = new MyMouseListener();
//		addMouseListener(ml);
//		addMouseMotionListener(ml);
//	}
//
//	private void setImage(Image img) {
//		g = (Graphics2D) img.getGraphics();
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setPaint(Color.black);
//		this.img = img;
//		repaint();
//	}
//
//	public void setBackground(Image img) {
//		background = copyImage(img);
//		setImage(copyImage(img));
//	}
//
//	private BufferedImage copyImage(Image img) {
//		BufferedImage copyOfImage = new BufferedImage(getSize().width,
//				getSize().height, BufferedImage.TYPE_INT_RGB);
//		Graphics g = copyOfImage.createGraphics();
//		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
//		return copyOfImage;
//	}
//
//	private void saveToStack(Image img) {
//		undoStack.push(copyImage(img));
//	}
//
	public void setThickness(int thick) {
		g.setStroke(new BasicStroke(thick));
	}

	public void setCurrentShapeType(String type)
	{
		currentShapeType=type;
	}

	public void setCurrentShapeColor(Color color)
	{
		currentShapeColor=color;
	}

//	class MyMouseListener extends MouseInputAdapter
//	{
//		private Point startPoint;
//
//		public void mousePressed(MouseEvent e)
//		{
//			startPoint = e.getPoint();
//			shape = new Rectangle();
//		}
//
//		public void mouseDragged(MouseEvent e)
//		{
//			int x = Math.min(startPoint.x, e.getX());
//			int y = Math.min(startPoint.y, e.getY());
//			int width = Math.abs(startPoint.x - e.getX());
//			int height = Math.abs(startPoint.y - e.getY());
//
//			shape.setBounds(x, y, width, height);
//			repaint();
//		}
//
//		public void mouseReleased(MouseEvent e)
//		{
//			if (shape.width != 0 || shape.height != 0)
//			{
//				addRectangle(shape, e.getComponent().getForeground());
//			}
//
//			shape = null;
//		}
//	}
}

	
