/*
 * Description: Client UI and main, All client UI's function except for canvas is here
 * */

package Client;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

import Remote.CanvasServerInterface;
import Remote.CanvasClientInterface;
import Remote.CanvasMsgInterface;

import java.util.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.rmi.*;
import java.net.MalformedURLException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;


public class CanvasClient extends UnicastRemoteObject implements CanvasClientInterface, Remote {

    //    private static final long serialVersionUID = 1L;
    static CanvasServerInterface server;
    private boolean isManager; //true if is manager
    private boolean havePermission; //permission granted by manager
    private JFrame frame;
    private DefaultListModel<String> userList;
    private DefaultListModel<String> chatList;
    private JButton clearBtn, saveBtn, saveAsBtn, openBtn, blackBtn, blueBtn, greenBtn, redBtn, orangeBtn, yellowBtn, cyanBtn;
    private JButton brownBtn, pinkBtn, greyBtn, purpleBtn, limeBtn, darkgreyBtn, magentaBtn, aoiBtn, skyBtn;
    private JButton drawBtn, lineBtn, rectBtn, circleBtn, triangleBtn, textBtn, eraserBtn;
    private JScrollPane msgArea;
    private JTextArea tellColor, displayColor;
    private JList<String> chat;
    private ArrayList<JButton> btnList;
    private Canvas canvasUI;
    private String clientName; // store client's name
    private String picName; // if saveAs then keep on saving on that location
    private String picPath; // if saveAs then keep on saving on that location
    private Hashtable<String, Point> startPoints = new Hashtable<String, Point>();

    // Constructor
    protected CanvasClient() throws RemoteException {
        //super();
        userList = new DefaultListModel<>();
        isManager = false;
        havePermission = true;
        chatList = new DefaultListModel<>();
        btnList = new ArrayList<>();
    }

    //keep on listening on the client UI's mouse actions
    //perform action accordingly, this changes the appearance of Client UI
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // the chosen mode will be boxed with black border
            LineBorder empty = new LineBorder(new Color(238, 238, 238), 2);
            LineBorder box = new LineBorder(Color.black, 2);

            if (e.getSource() == clearBtn) {
                canvasUI.reset();
                //if manager, clear all user's canvas
                // bug
                if (isManager) {
                    try {
                        server.refreshCanvas();
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
                    }
                }
            } else if (e.getSource() == openBtn) {
                try {
                    open();
                } catch (IOException ex) {
                    System.err.println("There is an IO error.");
                }
            } else if (e.getSource() == saveBtn) {
                try {
                    save();
                } catch (IOException ex) {
                    System.err.println("There is an IO error.");
                }
            } else if (e.getSource() == saveAsBtn) {
                try {
                    saveAs();
                } catch (IOException ex) {
                    System.err.println("There is an IO error.");
                }
                //change color of canvas
            } else if (e.getSource() == blackBtn) {
                canvasUI.black();
            } else if (e.getSource() == blueBtn) {
                canvasUI.blue();
            } else if (e.getSource() == greenBtn) {
                canvasUI.green();
            } else if (e.getSource() == redBtn) {
                canvasUI.red();
            } else if (e.getSource() == orangeBtn) {
                canvasUI.orange();
            } else if (e.getSource() == yellowBtn) {
                canvasUI.yellow();
            } else if (e.getSource() == cyanBtn) {
                canvasUI.cyan();
            } else if (e.getSource() == brownBtn) {
                canvasUI.brown();
            } else if (e.getSource() == pinkBtn) {
                canvasUI.pink();
            } else if (e.getSource() == greyBtn) {
                canvasUI.grey();
            } else if (e.getSource() == purpleBtn) {
                canvasUI.purple();
            } else if (e.getSource() == limeBtn) {
                canvasUI.lime();
            } else if (e.getSource() == darkgreyBtn) {
                canvasUI.darkGrey();
            } else if (e.getSource() == magentaBtn) {
                canvasUI.magenta();
            } else if (e.getSource() == aoiBtn) {
                canvasUI.aoi();
            } else if (e.getSource() == skyBtn) {
                canvasUI.sky();
            } else if (e.getSource() == drawBtn) {
                canvasUI.draw();
                for (JButton button : btnList) {
                    if (button == drawBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == lineBtn) {
                canvasUI.line();
                for (JButton button : btnList) {
                    if (button == lineBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == rectBtn) {
                canvasUI.rectangle();
                for (JButton button : btnList) {
                    if (button == rectBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == circleBtn) {
                canvasUI.circle();
                for (JButton button : btnList) {
                    if (button == circleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == triangleBtn) {
                canvasUI.triangle();
                for (JButton button : btnList) {
                    if (button == triangleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == textBtn) {
                canvasUI.text();
                for (JButton button : btnList) {
                    if (button == textBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == eraserBtn) {
                canvasUI.eraser();
                for (JButton button : btnList) {
                    if (button == eraserBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }

            // if change color, change display color accordingly
            if (e.getSource() == blackBtn || e.getSource() == yellowBtn || e.getSource() == greenBtn || e.getSource() == redBtn
                    || e.getSource() == orangeBtn || e.getSource() == blueBtn || e.getSource() == cyanBtn || e.getSource() == brownBtn || e.getSource() == pinkBtn
                    || e.getSource() == greyBtn || e.getSource() == purpleBtn || e.getSource() == limeBtn || e.getSource() == darkgreyBtn || e.getSource() == magentaBtn
                    || e.getSource() == aoiBtn || e.getSource() == skyBtn) {
                displayColor.setBackground(canvasUI.getCurrColor());
            }
        }
    };

    // the next three methods are for open, saving, and save as advanced features
    private void open() throws IOException {
        FileDialog opendialog = new FileDialog(frame, "open an image", FileDialog.LOAD);
        opendialog.setVisible(true);
        if (opendialog.getFile() != null) {
            this.picPath = opendialog.getDirectory();
            this.picName = opendialog.getFile();
            BufferedImage image = ImageIO.read(new File(picPath + picName));
            canvasUI.drawImage(image);
            ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
            //write the image to the imageArray
            ImageIO.write(image, "png", imageArray);
            server.sendOpenedImage(imageArray.toByteArray());
        }
    }

    private void saveAs() throws IOException {
        FileDialog saveAsdialog = new FileDialog(frame, "save image", FileDialog.SAVE);
        saveAsdialog.setVisible(true);
        if (saveAsdialog.getFile() != null) {
            this.picPath = saveAsdialog.getDirectory();
            this.picName = saveAsdialog.getFile();
            ImageIO.write(canvasUI.getCanvas(), "png", new File(picPath + picName));
        }
    }

    private void save() throws IOException {
        if (picName == null) {
            JOptionPane.showMessageDialog(null, "Please saveAs first.");
        } else {
            ImageIO.write(canvasUI.getCanvas(), "png", new File(picPath + picName));
        }
    }


    public void drawBoard(CanvasServerInterface server) {
        //build the GUI
        frame = new JFrame(clientName + "'s WhiteBoard");
        Container content = frame.getContentPane();

        canvasUI = new Canvas(clientName, isManager, server);

        //color button
        blackBtn = new JButton();
        blackBtn.setBackground(Color.black);
        blackBtn.setBorderPainted(false);
        blackBtn.setOpaque(true);
        blackBtn.addActionListener(actionListener);

        blueBtn = new JButton();
        blueBtn.setBackground(Color.blue);
        blueBtn.setBorderPainted(false);
        blueBtn.setOpaque(true);
        blueBtn.addActionListener(actionListener);

        greenBtn = new JButton();
        greenBtn.setBackground(Color.green);
        greenBtn.setBorderPainted(false);
        greenBtn.setOpaque(true);
        greenBtn.addActionListener(actionListener);

        redBtn = new JButton();
        redBtn.setBackground(Color.red);
        redBtn.setBorderPainted(false);
        redBtn.setOpaque(true);
        redBtn.addActionListener(actionListener);

        orangeBtn = new JButton();
        orangeBtn.setBackground(Color.orange);
        orangeBtn.setBorderPainted(false);
        orangeBtn.setOpaque(true);
        orangeBtn.addActionListener(actionListener);

        yellowBtn = new JButton();
        yellowBtn.setBackground(Color.yellow);
        yellowBtn.setBorderPainted(false);
        yellowBtn.setOpaque(true);
        yellowBtn.addActionListener(actionListener);

        cyanBtn = new JButton();
        cyanBtn.setBackground(Color.cyan);
        cyanBtn.setBorderPainted(false);
        cyanBtn.setOpaque(true);
        cyanBtn.addActionListener(actionListener);

        brownBtn = new JButton();
        brownBtn.setBackground(new Color(153, 76, 0));
        brownBtn.setBorderPainted(false);
        brownBtn.setOpaque(true);
        brownBtn.addActionListener(actionListener);

        pinkBtn = new JButton();
        pinkBtn.setBackground(new Color(255, 153, 204));
        pinkBtn.setBorderPainted(false);
        pinkBtn.setOpaque(true);
        pinkBtn.addActionListener(actionListener);

        greyBtn = new JButton();
        greyBtn.setBackground(Color.gray);
        greyBtn.setBorderPainted(false);
        greyBtn.setOpaque(true);
        greyBtn.addActionListener(actionListener);

        purpleBtn = new JButton();
        purpleBtn.setBackground(new Color(102, 0, 204));
        purpleBtn.setBorderPainted(false);
        purpleBtn.setOpaque(true);
        purpleBtn.addActionListener(actionListener);

        limeBtn = new JButton();
        limeBtn.setBackground(new Color(102, 102, 0));
        limeBtn.setBorderPainted(false);
        limeBtn.setOpaque(true);
        limeBtn.addActionListener(actionListener);

        darkgreyBtn = new JButton();
        darkgreyBtn.setBackground(Color.darkGray);
        darkgreyBtn.setBorderPainted(false);
        darkgreyBtn.setOpaque(true);
        darkgreyBtn.addActionListener(actionListener);

        magentaBtn = new JButton();
        magentaBtn.setBackground(Color.magenta);
        magentaBtn.setBorderPainted(false);
        magentaBtn.setOpaque(true);
        magentaBtn.addActionListener(actionListener);

        aoiBtn = new JButton();
        aoiBtn.setBackground(new Color(0, 102, 102));
        aoiBtn.setBorderPainted(false);
        aoiBtn.setOpaque(true);
        aoiBtn.addActionListener(actionListener);

        skyBtn = new JButton();
        skyBtn.setBackground(new Color(0, 128, 255));
        skyBtn.setBorderPainted(false);
        skyBtn.setOpaque(true);
        skyBtn.addActionListener(actionListener);

        // drawer types button, pencil, text, etc.
        LineBorder border = new LineBorder(Color.black, 2);

        Icon icon = new ImageIcon(this.getClass().getResource("/icon/pencil.png"));


        drawBtn = new JButton(icon);
        drawBtn.setToolTipText("Pencil draw");
        drawBtn.setBorder(border);
        drawBtn.addActionListener(actionListener);

        border = new LineBorder(new Color(238, 238, 238), 2);
        icon = new ImageIcon(this.getClass().getResource("/icon/line.png"));
        lineBtn = new JButton(icon);
        lineBtn.setToolTipText("Draw line");
        lineBtn.setBorder(border);
        lineBtn.addActionListener(actionListener);

        icon = new ImageIcon(this.getClass().getResource("/icon/rectangle.png"));
        rectBtn = new JButton(icon);
        rectBtn.setToolTipText("Draw rectangle");
        rectBtn.setBorder(border);
        rectBtn.addActionListener(actionListener);

        icon = new ImageIcon(this.getClass().getResource("/icon/circle.png"));
        circleBtn = new JButton(icon);
        circleBtn.setToolTipText("Draw circle");
        circleBtn.setBorder(border);
        circleBtn.addActionListener(actionListener);

        icon = new ImageIcon(this.getClass().getResource("/icon/triangle.png"));
        triangleBtn = new JButton(icon);
        rectBtn.setToolTipText("Draw triangle");
        triangleBtn.setBorder(border);
        triangleBtn.addActionListener(actionListener);

        icon = new ImageIcon(this.getClass().getResource("/icon/text.png"));
        textBtn = new JButton(icon);
        textBtn.setToolTipText("Text box");
        textBtn.setBorder(border);
        textBtn.addActionListener(actionListener);

        icon = new ImageIcon(this.getClass().getResource("/icon/eraser.png"));
        eraserBtn = new JButton(icon);
        eraserBtn.setToolTipText("Eraser");
        eraserBtn.setBorder(border);
        eraserBtn.addActionListener(actionListener);

        btnList.add(drawBtn);
        btnList.add(lineBtn);
        btnList.add(rectBtn);
        btnList.add(circleBtn);
        btnList.add(triangleBtn);
        btnList.add(textBtn);
        btnList.add(eraserBtn);

        // advanced feature button
        clearBtn = new JButton("New Board");
        clearBtn.setToolTipText("Create a new board");
        clearBtn.addActionListener(actionListener);
        saveBtn = new JButton("Save Image");
        saveBtn.setToolTipText("Save as image file");
        saveBtn.addActionListener(actionListener);
        saveAsBtn = new JButton("Save as");
        saveAsBtn.setToolTipText("Save image file");
        saveAsBtn.addActionListener(actionListener);
        openBtn = new JButton("Open Image");
        openBtn.setToolTipText("Open an image file");
        openBtn.addActionListener(actionListener);

        tellColor = new JTextArea("The current color is:");
        tellColor.setBackground(new Color(238, 238, 238));
        displayColor = new JTextArea("");
        displayColor.setBackground(Color.black);

        // if the client is the manager, he can save, open and clear the white board image
        if (isManager == false) {
            clearBtn.setVisible(false);
            saveBtn.setVisible(false);
            saveAsBtn.setVisible(false);
            openBtn.setVisible(false);
        }
        //List the other users. The client can see the list of user name of other users. The manager's user name is displayed with a flag
        JList<String> list = new JList<>(userList);
        JScrollPane currUsers = new JScrollPane(list);
        currUsers.setMinimumSize(new Dimension(100, 150));
        if (!isManager) {
            currUsers.setMinimumSize(new Dimension(100, 290));
        }
        // if user is manager, can kick off a user
        if (isManager) {
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    @SuppressWarnings("unchecked")
                    JList<String> list = (JList<String>) evt.getSource();
                    if (evt.getClickCount() == 2) {
                        int index = list.locationToIndex(evt.getPoint());
                        String selectedName = list.getModel().getElementAt(index);
                        try {
                            //manager can't remove him/herself
                            if (!getName().equals(selectedName)) {
                                int dialogResult = JOptionPane.showConfirmDialog(frame, "Are you sure to remove " + selectedName + "?",
                                        "Warning", JOptionPane.YES_NO_OPTION);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    try {
                                        server.deleteClient(selectedName);
                                        updateUserList(server.getClientList());
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        System.err.println("There is an IO error.");
                                    }
                                }
                            }
                        } catch (HeadlessException e) {
                            // TODO Auto-generated catch block
                            System.err.println("There is an headless error.");
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            System.err.println("There is an IO error.");
                        }
                    }
                }
            });
        }

        //chatbox with a send button
        chat = new JList<>(chatList);
        msgArea = new JScrollPane(chat);
        msgArea.setMinimumSize(new Dimension(100, 100));
        JTextField msgText = new JTextField();
        JButton sendBtn = new JButton("Send"); //addMouseListener here 直接call server 去broadcast message
        sendBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (!msgText.getText().equals("")) {
                    try {
                        server.addChat(clientName + ": " + msgText.getText());
                        // let the scrollpane to always show the newest chat message
                        SwingUtilities.invokeLater(() -> {
                            JScrollBar vertical = msgArea.getVerticalScrollBar();
                            vertical.setValue(vertical.getMaximum());
                        });
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(null, "WhiteBoard server is down, please save and exit.");
                    }
                    msgText.setText("");
                }
            }
        });

        GroupLayout layout = new GroupLayout(content);
        content.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(drawBtn)
                        .addComponent(lineBtn)
                        .addComponent(rectBtn)
                        .addComponent(circleBtn)
                        .addComponent(triangleBtn)
                        .addComponent(textBtn)
                        .addComponent(eraserBtn)
                )
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(canvasUI)
                        .addComponent(msgArea)
                        .addGroup(layout.createSequentialGroup()

                                .addComponent(msgText)
                                .addComponent(sendBtn)
                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(blackBtn)
                                .addComponent(yellowBtn)
                                .addComponent(cyanBtn)
                                .addComponent(brownBtn)
                                .addComponent(greyBtn)
                                .addComponent(purpleBtn)
                                .addComponent(limeBtn)

                                .addComponent(orangeBtn)


                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pinkBtn)
                                .addComponent(redBtn)
                                .addComponent(greenBtn)
                                .addComponent(blueBtn)
                                .addComponent(darkgreyBtn)
                                .addComponent(magentaBtn)
                                .addComponent(aoiBtn)
                                .addComponent(skyBtn)
                        )

                )
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(clearBtn)
                        .addComponent(openBtn)
                        .addComponent(saveBtn)
                        .addComponent(saveAsBtn)
                        .addComponent(currUsers)
                        .addComponent(tellColor)
                        .addComponent(displayColor)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(drawBtn)
                                .addComponent(lineBtn)
                                .addComponent(rectBtn)
                                .addComponent(circleBtn)
                                .addComponent(triangleBtn)
                                .addComponent(textBtn)
                                .addComponent(eraserBtn)
                        )
                        .addComponent(canvasUI)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(clearBtn)
                                .addComponent(openBtn)
                                .addComponent(saveBtn)
                                .addComponent(saveAsBtn)
                                .addComponent(currUsers)
                                .addComponent(tellColor)
                                .addComponent(displayColor)
                        )
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(msgArea)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(msgText)
                                .addComponent(sendBtn)
                        )
                )
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(blackBtn)
                                .addComponent(yellowBtn)
                                .addComponent(cyanBtn)
                                .addComponent(brownBtn)
                                .addComponent(greyBtn)
                                .addComponent(purpleBtn)
                                .addComponent(limeBtn)
                                .addComponent(orangeBtn)

                        )
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(pinkBtn)
                                .addComponent(redBtn)
                                .addComponent(greenBtn)
                                .addComponent(blueBtn)
                                .addComponent(darkgreyBtn)
                                .addComponent(magentaBtn)
                                .addComponent(aoiBtn)
                                .addComponent(skyBtn)
                        )
                )
        );

        layout.linkSize(SwingConstants.HORIZONTAL, clearBtn, saveBtn, saveAsBtn, openBtn); //format to same size

        // set the minimum framesize
        if (isManager) frame.setMinimumSize(new Dimension(820, 600));
        else frame.setMinimumSize(new Dimension(820, 600));

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        //if the manager close the window, all other client are removed and the all clients' window are force closed
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (isManager) {
                    if (JOptionPane.showConfirmDialog(frame,
                            "You are the manager? Are you sure close the application?", "Close Application?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            server.removeAll();

                        } catch (IOException e) {
                            System.err.println("There is an IO error");
                        } finally {
                            System.exit(0);
                        }
                    }
                } else {
                    if (JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to quit?", "Close Paint Board?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            server.removeMe(clientName);
                            updateUserList(server.getClientList());
                        } catch (RemoteException e) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
                        } finally {
                            System.exit(0);
                        }
                    }
                }
            }
        });
    }

    //
    public void updateUserList(Set<CanvasClientInterface> list) {
        this.userList.removeAllElements();
        for (CanvasClientInterface c : list) {
            try {
                userList.addElement(c.getName());
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // draw line or wiggles
    public Shape makeLine(Shape shape, Point start, Point end) {
        shape = new Line2D.Double(start.x, start.y, end.x, end.y);
        return shape;
    }

    //draw Rectangle
    public Shape makeRectangle(Shape shape, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Rectangle2D.Double(x, y, width, height);
        return shape;
    }

    //draw circle
    public Shape makeCircle(Shape shape, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Ellipse2D.Double(x, y, Math.max(width, height), Math.max(width, height));
        return shape;
    }

    //Make text
    public Shape makeText(Shape shape, Point start) {
        int x = start.x - 5;
        int y = start.y - 20;
        int width = 130;
        int height = 25;
        shape = new RoundRectangle2D.Double(x, y, width, height, 15, 15);
        return shape;
    }

    //Draw Triangle
    public Shape makeTriangle(Shape shape, Point start, Point end) {
        //store the start and end x and y
        int minx = Math.min(start.x, end.x);
        int miny = Math.min(start.y, end.y);
        int maxx = Math.max(start.x, end.x);
        int maxy = Math.max(start.y, end.y);
        int[] x = {minx, (minx + maxx) / 2, maxx};
        int[] y = {maxy, miny, maxy};
        shape = new Polygon(x, y, 3);
        //flipes depending on the drag location
        if (end.y < start.y) {
            int[] dy = {miny, maxy, miny};
            shape = new Polygon(x, dy, 3);
        }
        return shape;
    }

    //get the info sent from the other clients, and update the white board accordingly
    public void syncCanvas(CanvasMsgInterface msg) throws RemoteException {
        // skip msg from itself
        if (msg.getName().compareTo(clientName) == 0) {
            return;
        }
        Shape shape = null;

        if (msg.getState().equals("start")) {
            //Let startPoint stores the start point of client x and wait for the next draw action
            startPoints.put(msg.getName(), msg.getPoint());
            return;
        }

        //start from the start point of client x
        Point startPt = (Point) startPoints.get(msg.getName());

        //set canvas stroke color
        canvasUI.getGraphic().setPaint(msg.getColor());

        if (msg.getState().equals("drawing")) {
            if (msg.getMode().equals("eraser")) {
                canvasUI.getGraphic().setStroke(new BasicStroke(15.0f));
            }
            shape = makeLine(shape, startPt, msg.getPoint());
            startPoints.put(msg.getName(), msg.getPoint());
            canvasUI.getGraphic().draw(shape);
            canvasUI.repaint();
            return;
        }

        //the mouse is released so we draw from start point to the broadcast point
        if (msg.getState().equals("end")) {
            if (msg.getMode().equals("draw") || msg.getMode().equals("line")) {
                shape = makeLine(shape, startPt, msg.getPoint());
            } else if (msg.getMode().equals("eraser")) {
                canvasUI.getGraphic().setStroke(new BasicStroke(1.0f));
            } else if (msg.getMode().equals("rectangle")) {
                shape = makeRectangle(shape, startPt, msg.getPoint());
            } else if (msg.getMode().equals("circle")) {
                shape = makeCircle(shape, startPt, msg.getPoint());
            } else if (msg.getMode().equals("triangle")) {
                shape = makeTriangle(shape, startPt, msg.getPoint());
            } else if (msg.getMode().equals("text")) {
                canvasUI.getGraphic().setFont(new Font("TimesRoman", Font.PLAIN, 16));
                canvasUI.getGraphic().drawString(msg.getText(), msg.getPoint().x, msg.getPoint().y);
            }
            //draw shape if in shape mode: triangle, circle, rectangle
            if (!msg.getMode().equals("text")) {
                try {
                    canvasUI.getGraphic().draw(shape);
                } catch (Exception e) {

                }
            }
            //
            canvasUI.repaint();
            //once finished drawing remove the start point of client x
            startPoints.remove(msg.getName());
            return;
        }
        return;
    }

    public void setName(String name) throws RemoteException {
        this.clientName = name;
        return;
    }

    public String getName() throws RemoteException {
        return this.clientName;
    }


    public void addChat(String text) throws RemoteException {
        this.chatList.addElement(text);
    }

    public boolean getManager() {
        return this.isManager;
    }

    @Override
    public void assignManager() throws RemoteException {
        this.isManager = true;
    }


    public void refreshCanvas() {
        if (!this.isManager)
            this.canvasUI.reset();
    }

    public byte[] sendImage() throws IOException {
        ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
        ImageIO.write(this.canvasUI.getCanvas(), "png", imageArray);
        return imageArray.toByteArray();
    }

    public void drawOpenedImage(byte[] rawImage) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(rawImage));
        this.canvasUI.drawImage(image);
    }


    // new user join, should ask permission to manager
    public boolean askPermission(String name) {
        if (JOptionPane.showConfirmDialog(frame,
                name + " wants to join. Do you approve?", "Grant permission",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }

    }

    public void setPermission(boolean flag) {
        this.havePermission = flag;
    }

    public boolean getPermission() {
        return this.havePermission;
    }

    //close the application due to manager quit or kicked out by manager
    public void closeUI() throws IOException {
        //if manager does not give permission
        if (!this.havePermission) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, "Sorry, You were not grant access to the shared whiteboard." + "\n",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
            });
            t.start();
            return;
        }
        //if kicked out or manager quit
        Thread t = new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(frame, "The manager has quit.\n or you have been removed.\n" +
                                "Your application will be closed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        t.start();
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException {
        // submission:
//        if (args.length != 2) {
//            throw new IllegalArgumentException("Need exactly two arguments.");
//        }

        try {
//            if (!(args[0].equals("localhost") || args[0].equals("127.0.0.1"))) {
//                System.err.println("Please enter localhost or 127.0.0.1");
//                return;
//            }
//            String serverAddress = "//" + args[0] + ":" + args[1] + "/WhiteBoardServer";

            //testing:
            String hostName = "localhost";
            String portNumber = "1234";
            String serverAddress = "//" + hostName + ":" + portNumber + "/WhiteBoardServer";


            //Look up the Canvas Server from the RMI name registry
            server = (CanvasServerInterface) Naming.lookup(serverAddress);
            CanvasClientInterface client = new CanvasClient();
            //show user register GUI and register the user name to server
            boolean validName = false;
            String client_name = "";
            while (!validName) {
                client_name = JOptionPane.showInputDialog("Please type in your name:");
                if (client_name.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a name!");
                } else {
                    validName = true;
                }
                for (CanvasClientInterface c : server.getClientList()) {
                    if (client_name.equals(c.getName()) || c.getName().equals(client_name + " (Manager)")) {
                        validName = false;
                        JOptionPane.showMessageDialog(null, "The name is taken, think a different name!");
                    }
                }
            }
            client.setName(client_name);
            try {
                server.register(client);

            } catch (RemoteException e) {
                System.err.println("Error registering with remote server");
            }

            //launch the White Board GUI and start drawing
            client.drawBoard(server);
            //dont have permission access
            if (!client.getPermission()) {
                server.deleteClient(client.getName());

            }
        } catch (ConnectException e) {
            System.err.println("Server is down or wrong IP address or  Port number.");
        } catch (Exception e) {
            System.err.println("Please enter Valid IP and Port number.");
        }
    }

}
