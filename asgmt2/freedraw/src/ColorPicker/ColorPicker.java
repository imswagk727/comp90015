package ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ColorPicker extends JFrame {
    private ArrayList<Color> colors;
    private Color selectedColor;

    public ColorPicker() {
        colors = new ArrayList<>();
        colors.add(Color.BLACK);
        colors.add(Color.RED);
        colors.add(Color.ORANGE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.PINK);
        colors.add(Color.WHITE);

        selectedColor = colors.get(0);

        setTitle("Color Picker");
        setLayout(new GridLayout(1, 10));

        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedColor = color;
                }
            });
            add(colorButton);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 50);
        setVisible(true);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
}





