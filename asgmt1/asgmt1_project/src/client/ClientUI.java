package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.SwingConstants;

import java.awt.Image;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Shuangkun Fan (1131667)
 */
public class ClientUI extends Thread {


    private JFrame frame;
    private JTextField textFieldWord;
    static JTextArea textAreaMeaning;
    static JTextArea textMsg;

    /**
     * Launch the application.
     */
    public void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientUI window = new ClientUI();
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
    public ClientUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 582, 383);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Dictionary");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(227, 19, 90, 16);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblWord = new JLabel("Word");
        lblWord.setBounds(46, 76, 61, 16);
        frame.getContentPane().add(lblWord);

        JLabel lblMeaning = new JLabel("Meaning");
        lblMeaning.setBounds(46, 137, 61, 16);
        frame.getContentPane().add(lblMeaning);

        textFieldWord = new JTextField();
        textFieldWord.setBounds(138, 73, 197, 21);
        frame.getContentPane().add(textFieldWord);
        textFieldWord.setColumns(10);

        textAreaMeaning = new JTextArea();
        textAreaMeaning.setBounds(138, 137, 197, 79);
        frame.getContentPane().add(textAreaMeaning);

        textMsg = new JTextArea();
        textMsg.setBackground(new Color(238, 238, 238));
        textMsg.setBounds(138, 301, 359, 21);
        frame.getContentPane().add(textMsg);

        JButton btnQueryWord = new JButton("Query");
        btnQueryWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textMsg.setText(null);
                textAreaMeaning.setText(null);
                String word = textFieldWord.getText();
                if (word.isEmpty()) {
                    JOptionPane.showMessageDialog(btnQueryWord, "Please provide word for querying");
                    return;
                }
                String request = "QUERY" + "/" + word + "\n";
                try {
                    DictionaryClient.writer.write(request);
                    DictionaryClient.writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(btnQueryWord, "Server is not connected");
                    return;
                }
            }
        });
        btnQueryWord.setBounds(357, 71, 117, 29);
        frame.getContentPane().add(btnQueryWord);

        JButton btnUpdateWord = new JButton("Update");

        //update word
        btnUpdateWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textMsg.setText(null);
                String word = textFieldWord.getText();
                String meaning = textAreaMeaning.getText();

                //client's side error are handled on client side, reduce server load
                if (word.isEmpty() || meaning.isEmpty()) {
                    JOptionPane.showMessageDialog(btnUpdateWord, "Please provide both word and meaning");
                    return;
                }
                // if no error
                String request = "UPDATE" + "/" + word + "/" + meaning + "\n";
                try {
                    DictionaryClient.writer.write(request);
                    DictionaryClient.writer.flush();
                    textAreaMeaning.setText(null); // clear meaning field after updating
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(btnUpdateWord, "Server is not connected");
                    return;
                }
            }
        });

        btnUpdateWord.setBounds(357, 146, 117, 29);
        frame.getContentPane().add(btnUpdateWord);

        JButton btnRemoveWord = new JButton("Remove");
        btnRemoveWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textMsg.setText(null);
                textAreaMeaning.setText(null);
                //get word from Client UI
                String word = textFieldWord.getText();

                //check if there is a word
                if (word.isEmpty()) {
                    JOptionPane.showMessageDialog(btnRemoveWord, "Please provide word to removing");
                    return;
                }

                // if there is a word
                String request = "REMOVE" + "/" + word + "\n";
                try {
                    DictionaryClient.writer.write(request);
                    DictionaryClient.writer.flush();
                    textFieldWord.setText(null); // clear word field after removing
                } catch (Exception err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(btnRemoveWord, "Server is not connected");
                    return;
                }

            }
        });
        btnRemoveWord.setBounds(357, 187, 117, 29);
        frame.getContentPane().add(btnRemoveWord);

        JButton btnAddWord = new JButton("Add a new word");
        btnAddWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textMsg.setText(null);
                String word = textFieldWord.getText();
                String meaning = textAreaMeaning.getText();

                if (word.isEmpty() || meaning.isEmpty()) {
                    JOptionPane.showMessageDialog(btnAddWord, "Please provide word and meaning your added");
                    return;
                }

                String request = "ADD" + "/" + word + "/" + meaning + "\n";
                try {
                    DictionaryClient.writer.write(request);
                    DictionaryClient.writer.flush();
                    textFieldWord.setText(null);
                    textAreaMeaning.setText(null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(btnAddWord, "Server is not connected");
                    return;
                }
            }
        });
        btnAddWord.setBounds(165, 239, 144, 29);
        frame.getContentPane().add(btnAddWord);

//        not used
//        JLabel lblNewLabel_1 = new JLabel("Message:");
//        lblNewLabel_1.setBounds(46, 301, 61, 16);
//        frame.getContentPane().add(lblNewLabel_1);


        JLabel bookLable = new JLabel("");
        bookLable.setHorizontalAlignment(SwingConstants.CENTER);
        Image book = new ImageIcon(this.getClass().getResource("/img/dictionary.png")).getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        bookLable.setIcon(new ImageIcon(book));
        bookLable.setBounds(309, 0, 61, 55);
        frame.getContentPane().add(bookLable);


    }
}
