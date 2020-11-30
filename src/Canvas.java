import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.Point;
//import java.awt.Rectangle;
import java.awt.RenderingHints;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionAdapter;
//import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
//import java.util.ArrayList;
//
import javax.imageio.ImageIO;
//import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;


public class Canvas extends JPanel {
//	private int X1, Y1, X2, Y2;
	private Graphics2D g2D;
	private String currentShapeType;
	private Shape currentShapeObject; //stores the current shape object
	private Color currentShapeColor; //current shape color
	private int currentShapeThickness;
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
//	private final Stack<Shape> undoStack = new Stack<Shape>();
	private final Stack<Shape> redoStack = new Stack<Shape>();
	private int pencilEraserX1 = 0, pencilEraserY1 = 0, pencilEraserX2 = 0, pencilEraserY2 = 0;

	public void save(File file) {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(exShapes);
//			ImageIO.write((RenderedImage) img, "PNG", file);
		} catch (IOException ex) {
		}
	}

	public void load(File file) {
		try {
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
			exShapes=(LinkedList<Shape>) ois.readObject();
			repaint();
//			Object allStus= ois.readObject();

//			while(iter.hasNext()){
//				Student te=iter.next();
//				System.out.println("ID="+te.getID()+"/tname="+te.getName());
//			}
//			img = ImageIO.read(file);
//			g2D = (Graphics2D) img.getGraphics();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
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
		currentShapeThickness=1;
		MouseHandler handler = new MouseHandler();
		addMouseListener( handler );
		addMouseMotionListener( handler );
//		defaultListener();
	}
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

//		if (img == null) {
//			img = createImage(getSize().width, getSize().height);
//			g2D = (Graphics2D) img.getGraphics();
//			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);
//
//			clear();
//
//		}
//		g.drawImage(img, 0, 0, null);

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
			if("line".equals(currentShapeType) || currentShapeType==null) {
				currentShapeObject= new Line( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			} else if("rectangle".equals(currentShapeType)) {
				currentShapeObject= new Rectangle( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			} else if("oval".equals(currentShapeType)) {
				currentShapeObject= new Oval( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			} else if("circle".equals(currentShapeType)) {
				currentShapeObject= new Circle( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			} else if("pencil".equals(currentShapeType) || "eraser".equals(currentShapeType)) {
				pencilEraserX1=event.getX();
				pencilEraserY1=event.getY();
				pencilEraserX2 = event.getX();
				pencilEraserY2 = event.getY();
				if("pencil".equals(currentShapeType)) {
					currentShapeObject= new Pencil( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				} else if("eraser".equals(currentShapeType)) {
					currentShapeObject= new Eraser( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				}
//				currentShapeObject= new Line( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				pencilEraserX1 = pencilEraserX2;
				pencilEraserY1 = pencilEraserY2;
				exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes
				currentShapeObject.isPencilEraserStart = true;
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
			if(currentShapeObject == null) return ;

			if("pencil".equals(currentShapeType) || "eraser".equals(currentShapeType)) {
				pencilEraserX1=event.getX();
				pencilEraserY1=event.getY();
				pencilEraserX2 = event.getX();
				pencilEraserY2 = event.getY();
				if("pencil".equals(currentShapeType)) {
					currentShapeObject= new Pencil( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				} else if("eraser".equals(currentShapeType)) {
					currentShapeObject= new Eraser( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				}
				currentShapeObject.isPencilEraserfinish = true;
				exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes
			} else {
				//sets currentShapeObject x2 & Y2
				currentShapeObject.setX2(event.getX());
				currentShapeObject.setY2(event.getY());
				exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes
			}

			currentShapeObject=null; //sets currentShapeObject to null
//			clearedShapes.makeEmpty(); //clears clearedShapes
			if(!redoStack.isEmpty()) {
				redoStack.clear();
			}
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
			if("pencil".equals(currentShapeType) || "eraser".equals(currentShapeType)) {
				pencilEraserX2 = event.getX();
				pencilEraserY2 = event.getY();
				if("pencil".equals(currentShapeType)) {
					currentShapeObject= new Pencil( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				} else if("eraser".equals(currentShapeType)) {
					currentShapeObject= new Eraser( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				}
//				currentShapeObject= new Line( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				pencilEraserX1 = pencilEraserX2;
				pencilEraserY1 = pencilEraserY2;
				exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes
//				currentShapeObject=null; //sets currentShapeObject to null
			} else {
				//sets currentShapeObject x2 & Y2
				currentShapeObject.setX2(event.getX());
				currentShapeObject.setY2(event.getY());
			}


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
	public void clear() {
		if (exShapes.size() > 0) {
			exShapes.clear();
			repaint();
		}
	}

	public void undo() {
		if (exShapes.size() > 0) {
			if(exShapes.getFirst() instanceof Pencil || exShapes.getFirst() instanceof Eraser) {
				System.out.println("exShapes.getFirst() instanceof Pencil");
				while(exShapes.size() > 0 &&
						(exShapes.getFirst() instanceof Pencil || exShapes.getFirst() instanceof Eraser) &&
						!exShapes.getFirst().isPencilEraserStart) {
					redoStack.push(exShapes.getFirst());
					exShapes.removeFirst();
				}
			}
			redoStack.push(exShapes.getFirst());
			exShapes.removeFirst();
			repaint();
		}
	}

	public void redo() {
		if (redoStack.size() > 0) {
			if(redoStack.peek() instanceof Pencil || redoStack.peek() instanceof Eraser) {
				while(redoStack.size() > 0 &&
						(redoStack.peek() instanceof Pencil || redoStack.peek() instanceof Eraser) &&
						!redoStack.peek().isPencilEraserfinish) {
					exShapes.addFirst(redoStack.peek());
					redoStack.pop();
				}
				if(redoStack.size() > 0 && redoStack.peek().isPencilEraserfinish) {
					exShapes.addFirst(redoStack.peek());
					redoStack.pop();
				}
			}
			else {
				exShapes.addFirst(redoStack.peek());
				redoStack.pop();
			}
			repaint();
		}
	}
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
//		g2D = (Graphics2D) img.getGraphics();
//		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D.setPaint(Color.black);
//		this.img = img;
//		repaint();
//	}

//	public void setBackground(Image img) {
//		background = copyImage(img);
//		setImage(copyImage(img));
//	}

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
//	public void setThickness(int thick) {
//		g2D.setStroke(new BasicStroke(thick));
//	}

	public void setCurrentShapeType(String type)
	{
		currentShapeType=type;
	}

	public void setCurrentShapeColor(Color color)
	{
		currentShapeColor=color;
	}
	public void setCurrentShapeThickness(int thick) {
		currentShapeThickness=thick;
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

	
