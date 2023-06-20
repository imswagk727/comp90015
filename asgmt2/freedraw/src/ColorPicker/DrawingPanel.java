package ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class DrawingPanel extends JPanel {
    private ArrayList<ArrayList<Point>> strokes;
    private ArrayList<Color> strokeColors;
    private ArrayList<Integer> strokeSizes;
    private ArrayList<Point> currentStroke;
    private Color selectedColor;
    private int selectedSize;

    public DrawingPanel() {
        strokes = new ArrayList<>();
        strokeColors = new ArrayList<>();
        strokeSizes = new ArrayList<>();
        currentStroke = new ArrayList<>();
        selectedColor = Color.BLACK;
        selectedSize = 1;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                currentStroke = new ArrayList<>();
                currentStroke.add(e.getPoint());
                strokes.add(currentStroke);
                strokeColors.add(selectedColor);
                strokeSizes.add(selectedSize);
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentStroke.add(e.getPoint());
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < strokes.size(); i++) {
            ArrayList<Point> stroke = strokes.get(i);
            Color color = strokeColors.get(i);
            int size = strokeSizes.get(i);
            g.setColor(color);
            ((Graphics2D) g).setStroke(new BasicStroke(size));
            for (int j = 0; j < stroke.size() - 1; j++) {
                Point p1 = stroke.get(j);
                Point p2 = stroke.get(j + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public void setColor(Color color) {
        selectedColor = color;
    }

    public void setSize(int size) {
        selectedSize = size;
    }
}