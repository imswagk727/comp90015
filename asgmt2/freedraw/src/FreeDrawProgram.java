import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class FreeDrawProgram {
    private JFrame frame;
    private JPanel drawingPanel;
    private List<List<Point>> segments;

    public FreeDrawProgram() {
        segments = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Free Draw Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);

                for (List<Point> segment : segments) {
                    if (!segment.isEmpty()) {
                        Point start = segment.get(0);
                        for (int i = 1; i < segment.size(); i++) {
                            Point end = segment.get(i);
                            g2d.drawLine(start.x, start.y, end.x, end.y);
                            start = end;
                        }
                    }
                }
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                segments.add(new ArrayList<>());
                segments.get(segments.size() - 1).add(e.getPoint());
                drawingPanel.repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                segments.get(segments.size() - 1).add(e.getPoint());
                drawingPanel.repaint();
            }
        });

        frame.getContentPane().add(drawingPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FreeDrawProgram();
            }
        });
    }
}
