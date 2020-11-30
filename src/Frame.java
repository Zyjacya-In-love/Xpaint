import sun.misc.Cleaner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Frame {
    private int width = 900, height = 800;
    private Canvas canvas = new Canvas();
    private JButton colorPicker, clearButton, undoButton, redoButton, saveButton, loadButton, saveAsButton;
    private JSlider thicknessSlider;
    private JLabel thicknessStat = new JLabel("1");
    Color[] colorArray = { Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, Color.LIGHT_GRAY};
    String[] shapeArray = { "pencil", "eraser", "line", "rectangle", "oval", "circle"};
    ButtonHandler buttonHandler = new ButtonHandler();
    boolean isFirstSave = true;
    private JFileChooser fileChooser;
    private File file;
    private JFrame frame;

    ChangeListener thick = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            thicknessStat.setText(String.format("%s", thicknessSlider.getValue()));
            canvas.setCurrentShapeThickness(thicknessSlider.getValue());
        }
    };

//    ActionListener listener = new ActionListener() {
//
//        public void actionPerformed(ActionEvent event) {
//            System.out.println("dsadsadsadasdsadas");
//        }
//    };

    Frame() {
        // set skin
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        JPanel northPanel = new JPanel();

        Box leftVerticalBox = Box.createVerticalBox();
        Box thicknessSliderBox = Box.createHorizontalBox();


        // LEFT thickness slider
        thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 9, 1);
        thicknessSlider.setMajorTickSpacing(25);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPreferredSize(new Dimension(40, 40));
        thicknessSlider.addChangeListener(thick);
        thicknessSliderBox.add(thicknessSlider, BorderLayout.NORTH);
        thicknessSliderBox.add(thicknessStat, BorderLayout.NORTH);
        leftVerticalBox.add(Box.createVerticalStrut(40));
        leftVerticalBox.add(thicknessSliderBox, BorderLayout.NORTH);
        leftVerticalBox.add(Box.createVerticalStrut(20));

        // LEFT shape
        for (int i = 0; i < shapeArray.length; i++) {
            JButton shapeButton = new JButton(shapeArray[i]);
            // set button size
            shapeButton.setPreferredSize(new Dimension(100, 40));
            // add button to leftVerticalBox
            leftVerticalBox.add(shapeButton);
            // add listener to button
            shapeButton.addActionListener(buttonHandler);
        }

        // LEFT clear undo redo
        clearButton = new JButton("CLEAR");
        clearButton.addActionListener(buttonHandler);
        undoButton = new JButton("UNDO");
        undoButton.addActionListener(buttonHandler);
        redoButton = new JButton("REDO");
        redoButton.addActionListener(buttonHandler);
        leftVerticalBox.add(Box.createVerticalStrut(40));
        leftVerticalBox.add(clearButton, BorderLayout.NORTH);
        leftVerticalBox.add(Box.createVerticalStrut(20));
        leftVerticalBox.add(redoButton, BorderLayout.NORTH);
        leftVerticalBox.add(Box.createVerticalStrut(5));
        leftVerticalBox.add(undoButton, BorderLayout.NORTH);

        // TOP color
        for (int i = 0; i < colorArray.length; i++) {
            JButton colorButton = new JButton();
            colorButton.setBackground(colorArray[i]);
            colorButton.setPreferredSize(new Dimension(40, 40));
            colorButton.addActionListener(buttonHandler);
            northPanel.add(colorButton);
        }
        colorPicker = new JButton("Color Picker");
        colorPicker.addActionListener(buttonHandler);
        northPanel.add(colorPicker);

        // TOP About file save & load
        saveButton = new JButton("Save");
        saveButton.addActionListener(buttonHandler);
        loadButton = new JButton("Load");
        loadButton.addActionListener(buttonHandler);
        northPanel.add(saveButton);
        northPanel.add(loadButton);


        frame = new JFrame("Xpaint");
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        container.add(canvas, BorderLayout.CENTER);
        container.add(northPanel, BorderLayout.NORTH);
        container.add(leftVerticalBox, BorderLayout.WEST);

        frame.setVisible(true);
        frame.setSize(width+79,height+11);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ButtonHandler implements ActionListener {
        // handles button events
        public void actionPerformed( ActionEvent e ) {
            System.out.println("ButtonHandler");
            System.out.println(e.getActionCommand());
//            System.out.println("".equals(e.getActionCommand()));
            // set color
            if ("".equals(e.getActionCommand())) {
                JButton button = (JButton) e.getSource();
                canvas.setCurrentShapeColor(button.getBackground());
            } else if ("CLEAR".equals(e.getActionCommand())){
                canvas.clear();
            } else if ("UNDO".equals(e.getActionCommand())){
                canvas.undo();
            } else if ("REDO".equals(e.getActionCommand())){
                canvas.redo();
            } else if ("Save".equals(e.getActionCommand())) {
                if (isFirstSave) {
                    fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(saveButton) == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                        isFirstSave = false;
                        frame.setTitle("Xpaint" + file.toString());
                        canvas.save(file);
                    }
                } else {
                    frame.setTitle("Xpaint" + file.toString());
                    canvas.save(file);
                }
            } else if ("Load".equals(e.getActionCommand())) {
                fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(loadButton) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    frame.setTitle("Xpaint" + file.toString());
                    canvas.load(file);
                }
            } else if ("Color Picker".equals(e.getActionCommand())) {
                Color color = Color.BLACK;
                color = JColorChooser.showDialog(null, "Pick your color!", color);
                if (color == null)
                    color = (Color.BLACK);
                canvas.setCurrentShapeColor(color);
            }
            // set shape
            else {
                canvas.setCurrentShapeType(e.getActionCommand());
            }
//            else if (event.getActionCommand().equals("Redo")){
//                panel.redoLastShape();
//            }
//            else if (event.getActionCommand().equals("Clear")){
//                panel.clearDrawing();
//            }
//            //
//            if("line".equals(e.getActionCommand())) {
//                canvas.setCurrentShapeType(e.getActionCommand());
//            }
//            if (event.getActionCommand().equals("Undo")){
//                canvas.clearLastShape();
//            }
//            else if (event.getActionCommand().equals("Redo")){
//                canvas.redoLastShape();
//            }
//            else if (event.getActionCommand().equals("Clear")){
//                canvas.clearDrawing();
//            }

        } // end method actionPerformed
    } // end private inner class ButtonHandler
}
