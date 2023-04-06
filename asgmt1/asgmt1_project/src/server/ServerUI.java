package server;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.ImageIcon;

/**
 * Shuangkun Fan (1131667)
 */
public class ServerUI extends Thread {

    private JFrame frmDictionaryServer;
    static JLabel lblConnectionNum;
    private JLabel lblNewLabel_1;
    private JLabel lblConnectionAddr;

    /**
     * Launch the application.
     */
    public void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerUI window = new ServerUI();
                    window.frmDictionaryServer.setVisible(true);
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
        frmDictionaryServer = new JFrame();
        frmDictionaryServer.setTitle("Dictionary Server");
        frmDictionaryServer.setBounds(100, 100, 341, 228);
        frmDictionaryServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDictionaryServer.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Connection");
        lblNewLabel.setBounds(101, 73, 97, 16);
        frmDictionaryServer.getContentPane().add(lblNewLabel);

        lblConnectionNum = new JLabel("0");
        lblConnectionNum.setBounds(197, 73, 61, 16);
        frmDictionaryServer.getContentPane().add(lblConnectionNum);

        JLabel lblServer = new JLabel("");
        Image server = new ImageIcon(this.getClass().getResource("/img/server.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        lblServer.setIcon(new ImageIcon(server));
        lblServer.setBounds(24, 52, 71, 97);
        frmDictionaryServer.getContentPane().add(lblServer);

        lblNewLabel_1 = new JLabel("Address");
        lblNewLabel_1.setBounds(101, 119, 61, 16);
        frmDictionaryServer.getContentPane().add(lblNewLabel_1);

        lblConnectionAddr = new JLabel(String.valueOf(DictionaryServer.port));
        lblConnectionAddr.setBounds(197, 119, 120, 16);
        frmDictionaryServer.getContentPane().add(lblConnectionAddr);


//        JButton btnCloserServer = new JButton("Close Server");
//        btnCloserServer.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//        });
//        btnCloserServer.setBounds(124, 194, 117, 29);
//        frame.getContentPane().add(btnCloserServer);
    }
}
