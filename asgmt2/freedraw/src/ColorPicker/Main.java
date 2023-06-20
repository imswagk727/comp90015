package ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Free Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DrawingPanel drawingPanel = new DrawingPanel();
        frame.add(drawingPanel);

        JPanel colorPickerPanel = new JPanel();
        colorPickerPanel.setLayout(new GridLayout(16, 1));

        Color[] colors = {Color.BLACK, Color.GRAY, Color.DARK_GRAY, Color.LIGHT_GRAY,
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                Color.CYAN, Color.MAGENTA, Color.PINK, Color.decode("#FF8C00"), Color.decode("#00FF00"),
                Color.decode("#00BFFF"), Color.decode("#800080")};

        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.setColor(color);
                }
            });
            colorPickerPanel.add(colorButton);
        }

        JPanel sizePickerPanel = new JPanel();
        sizePickerPanel.setLayout(new GridLayout(1, 4));

        int[] sizes = {1, 3, 5, 7};

        for (int size : sizes) {
            JButton sizeButton = new JButton(String.valueOf(size));
            sizeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    drawingPanel.setSize(size);
                }
            });
            sizePickerPanel.add(sizeButton);
        }

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BorderLayout());
        controlsPanel.add(colorPickerPanel, BorderLayout.EAST);
//        controlsPanel.add(sizePickerPanel, BorderLayout.CENTER);

        frame.add(controlsPanel, BorderLayout.WEST);
        frame.setVisible(true);
    }
}