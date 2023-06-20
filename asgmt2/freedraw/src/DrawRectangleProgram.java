import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawRectangleProgram {
    private JFrame frame;
    private JPanel drawingPanel;
    private ArrayList<Rectangle> rectangles;

    private Point startPoint;
    private Point endPoint;
    private boolean isDrawing;

    public DrawRectangleProgram() {
        rectangles = new ArrayList<>();
        isDrawing = false;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Draw Rectangle Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);

                for (Rectangle rect : rectangles) {
                    g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
                }

                if (isDrawing) {
                    int x = Math.min(startPoint.x, endPoint.x);
                    int y = Math.min(startPoint.y, endPoint.y);
                    int width = Math.abs(startPoint.x - endPoint.x);
                    int height = Math.abs(startPoint.y - endPoint.y);

                    Rectangle previewRect = new Rectangle(x, y, width, height);
                    g2d.drawRect(previewRect.x, previewRect.y, previewRect.width, previewRect.height);
                }
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                endPoint = startPoint;
                isDrawing = true;
                drawingPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                isDrawing = false;
                createRectangle();
                drawingPanel.repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing) {
                    endPoint = e.getPoint();
                    drawingPanel.repaint();
                }
            }
        });

        frame.getContentPane().add(drawingPanel);
        frame.setVisible(true);
    }

    private void createRectangle() {
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);

        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangles.add(rectangle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DrawRectangleProgram();
            }
        });
    }
}
