package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerUI extends Thread {

    private JFrame frame;
    static JLabel lblConnectionNum;
    /**
     * Launch the application.
     */
    public void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerUI window = new ServerUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ServerUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Connection");
        lblNewLabel.setBounds(43, 90, 97, 16);
        frame.getContentPane().add(lblNewLabel);

        lblConnectionNum = new JLabel("0");
        lblConnectionNum.setBounds(185, 90, 61, 16);
        frame.getContentPane().add(lblConnectionNum);

        JButton btnNewButton = new JButton("Close Server");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(124, 194, 117, 29);
        frame.getContentPane().add(btnNewButton);
    }
}
