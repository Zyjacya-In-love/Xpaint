import java.io.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;


public class Canvas extends JPanel {
	private String currentShapeType; //current shape type
	private Shape currentShapeObject; //stores the current shape object
	private Color currentShapeColor; //current shape color
	private int currentShapeThickness;
	private LinkedList<Shape> exShapes; //dynamic stack of shapes
	private final Stack<Shape> redoStack = new Stack<Shape>();
	private int pencilEraserX1 = 0, pencilEraserY1 = 0, pencilEraserX2 = 0, pencilEraserY2 = 0;
	private int clickNumber = 0;
	private int[] triangleX = new int[4], triangleY = new int[4];

	public void save(File file) {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(exShapes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load(File file) {
		try {
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
			exShapes=(LinkedList<Shape>) ois.readObject();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Canvas() {
		setBackground(Color.WHITE);
		exShapes = new LinkedList<Shape>();

		currentShapeObject=null;
		currentShapeColor=Color.BLACK;
		currentShapeThickness=1;

		MouseHandler handler = new MouseHandler();
		addMouseListener( handler );
		addMouseMotionListener( handler );
	}

	public void paintComponent( Graphics g ) {
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
			if(currentShapeType==null) {
				setCurrentShapeType("pencil");
			}

			if("line".equals(currentShapeType)) {
				currentShapeObject= new Line( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			} else if("rectangle".equals(currentShapeType)) {
				currentShapeObject= new Rectangle( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
			}else if("square".equals(currentShapeType)) {
                currentShapeObject= new Square( event.getX(), event.getY(), event.getX(), event.getY(), currentShapeColor, currentShapeThickness);
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
				pencilEraserX1 = pencilEraserX2;
				pencilEraserY1 = pencilEraserY2;
				exShapes.addFirst(currentShapeObject); //addFront currentShapeObject onto myShapes
				currentShapeObject.isPencilEraserStart = true;
			}
		}

		public void mouseReleased( MouseEvent event ) {
			if(currentShapeObject == null) return ;
			if ("triangle".equals(currentShapeType)) {
			    repaint();
			    return ;
            }

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
			if(!redoStack.isEmpty()) {
				redoStack.clear();
			}
			repaint();
		}

		public void mouseMoved( MouseEvent event ) {
            if("triangle".equals(currentShapeType)) {
                if (clickNumber > 0) {
                    triangleX[clickNumber] = event.getX();
                    triangleY[clickNumber] = event.getY();
                    currentShapeObject = new Triangle(triangleX, triangleY, clickNumber+1, currentShapeColor, currentShapeThickness);
                }
            }
            repaint();
		}

		public void mouseClicked(MouseEvent event) {
		    if("triangle".equals(currentShapeType)) {
		        if (clickNumber == 2) {
                    triangleX[3] = triangleX[0];
                    triangleY[3] = triangleY[0];
                    clickNumber = 0;
                    currentShapeObject = new Triangle(triangleX, triangleY, 4, currentShapeColor, currentShapeThickness);
                    triangleX = new int[4];
                    triangleY = new int[4];
                    exShapes.addFirst(currentShapeObject);
                    currentShapeObject=null; //sets currentShapeObject to null
                    if(!redoStack.isEmpty()) {
                        redoStack.clear();
                    }
                    return ;
                }
                triangleX[clickNumber] = event.getX();
                triangleY[clickNumber] = event.getY();
                clickNumber++;
            }
            repaint();
        }

        public void mouseDragged(MouseEvent event ) {
            if("triangle".equals(currentShapeType)) return ;

            if("pencil".equals(currentShapeType) || "eraser".equals(currentShapeType)) {
				pencilEraserX2 = event.getX();
				pencilEraserY2 = event.getY();
				if("pencil".equals(currentShapeType)) {
					currentShapeObject= new Pencil( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				} else if("eraser".equals(currentShapeType)) {
					currentShapeObject= new Eraser( pencilEraserX1, pencilEraserY1, pencilEraserX2, pencilEraserY2, currentShapeColor, currentShapeThickness);
				}
				pencilEraserX1 = pencilEraserX2;
				pencilEraserY1 = pencilEraserY2;
				exShapes.addFirst(currentShapeObject);
			} else {
				//sets currentShapeObject x2 & Y2
				currentShapeObject.setX2(event.getX());
				currentShapeObject.setY2(event.getY());
			}
			repaint();
		}
	}

	public void clear() {
		if (exShapes.size() > 0) {
			exShapes.clear();
			repaint();
		}
	}

	public void undo() {
		if (exShapes.size() > 0) {
			if(exShapes.getFirst() instanceof Pencil || exShapes.getFirst() instanceof Eraser) {
				while(exShapes.size() > 0 &&
						(exShapes.getFirst() instanceof Pencil || exShapes.getFirst() instanceof Eraser) &&
						!exShapes.getFirst().isPencilEraserStart) {
					redoStack.push(exShapes.getFirst());
					exShapes.removeFirst();
				}
				if(exShapes.size() > 0 && exShapes.getFirst().isPencilEraserStart) {
					redoStack.push(exShapes.getFirst());
					exShapes.removeFirst();
				}
			} else {
				redoStack.push(exShapes.getFirst());
				exShapes.removeFirst();
			}
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
			} else {
				exShapes.addFirst(redoStack.peek());
				redoStack.pop();
			}
			repaint();
		}
	}

	public void setCurrentShapeType(String type) {
		currentShapeType=type;
	}
	public void setCurrentShapeColor(Color color) {
		currentShapeColor=color;
	}
	public void setCurrentShapeThickness(int thick) {
		currentShapeThickness=thick;
	}
}

	
