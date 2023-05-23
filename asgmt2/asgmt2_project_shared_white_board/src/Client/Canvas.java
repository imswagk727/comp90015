/*
 * The 'whiteboard' component in the Client UI
 */

package Client;

import Remote.CanvasServerInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class Canvas extends JComponent {
    private static final long serialVersionUID = 1L;
    private String clientName;
    private boolean isManager;
    private Point startPt, endPt;
    private Color color;
    private String mode;
    private String text;

    private BufferedImage image; //store the dimension and data far canvas to save private BufferedImage previousCanvas;
    private BufferedImage previousCanvas;   // save the state of current/previous canvas;
    private Graphics2D graphics;
    private CanvasServerInterface server;


    public Canvas(String name, boolean isManager, CanvasServerInterface RemoteInterface) {
        this.server = RemoteInterface;
        this.clientName = name;
        this.isManager = isManager;
        this.color = Color.black;
        this.mode = "draw";
        this.text = "";

        setDoubleBuffered(false);
        //when listens a mouse click, store the start location and sent it to the server
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                startPt = e.getPoint();
                saveCanvas();
                try {
                    MessageWrapper message = new MessageWrapper("start", clientName, mode, color, startPt, text);
                    server.broadCastCanvas(message);
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Canvas server is down");
                }
            }
        });

        //listen to the action on the canvas, draw the shape on local client, then send the shape to server
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPt = e.getPoint();


                Shape shape = null;
                if (graphics != null) {
                    if (mode.equals("draw")) {

                        shape = makeLine(shape, startPt, endPt);
                        startPt = endPt;
                        try {
                            MessageWrapper item = new MessageWrapper("drawing", clientName, mode, color, endPt, "");
                            server.broadCastCanvas(item);
                        } catch (RemoteException ex) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down");
                        }
                    } else if (mode.equals("eraser")) {
                        shape = makeLine(shape, startPt, endPt);
                        startPt = endPt;
                        graphics.setPaint(Color.white);
                        graphics.setStroke(new BasicStroke(15.0f));
                        try {
                            MessageWrapper message = new MessageWrapper("drawing", clientName, mode, Color.white, endPt, "");
                            server.broadCastCanvas(message);

                        } catch (RemoteException ex) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down");
                        }
                    } else if (mode.equals("line")) {

                        drawPreviousCanvas();
                        shape = makeLine(shape, startPt, endPt);
                    } else if (mode.equals("rectangle")) {
                        drawPreviousCanvas();
                        shape = makeRectangle(shape, startPt, endPt);
                    } else if (mode.equals("circle")) {
                        drawPreviousCanvas();
                        shape = makeCircle(shape, startPt, endPt);

                    } else if (mode.equals("triangle")) {
                        drawPreviousCanvas();
                        shape = makeTriangle(shape, startPt, endPt);
                    } else if (mode.equals("text")) {
                        drawPreviousCanvas();
                        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                        graphics.drawString("Enter text here", endPt.x, endPt.y);
                        shape = makeText(shape, startPt);
                        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{3}, 0);
                        graphics.setStroke(dashed);
                    }

                    graphics.draw(shape);

                    repaint();
                }
            }
        });

        // draw shape on mouse released
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {

                // released mouse point
                endPt = e.getPoint();
                Shape shape = null;
                if (graphics != null) {
                    if (mode.equals("line")) {
                        shape = makeLine(shape, startPt, endPt);

                    } else if (mode.equals("draw")) {
                        shape = makeLine(shape, startPt, endPt);
                    } else if (mode.equals("rectangle")) {
                        shape = makeRectangle(shape, startPt, endPt);
                    } else if (mode.equals("circle")) {
                        shape = makeCircle(shape, startPt, endPt);
                    } else if (mode.equals("triangle")) {
                        shape = makeTriangle(shape, startPt, endPt);
                    } else if (mode.equals("text")) {
                        text = JOptionPane.showInputDialog("Please enter your text.");
                        if (text == null)
                            text = "";
                        drawPreviousCanvas();
                        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                        graphics.drawString(text, endPt.x, endPt.y);
                        graphics.setStroke(new BasicStroke(1.0f));
                    }

                    if (!mode.equals("text")) {
                        try {
                            graphics.draw(shape);
                        } catch (NullPointerException ex) {

                        }
                    }
                    repaint();
                    // eraser
                    if (mode.equals("eraser")) {
                        try {
                            MessageWrapper message = new MessageWrapper("end", clientName, mode, Color.white, endPt, text);
                            server.broadCastCanvas(message);
                        } catch (RemoteException e1) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down");
                        }
                        graphics.setPaint(color);
                        graphics.setStroke(new BasicStroke(1.0f));

                    } else {
                        try {
                            MessageWrapper message = new MessageWrapper("end", clientName, mode, color, endPt, text);
                            server.broadCastCanvas(message);
                        } catch (RemoteException e1) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down");
                        }
                    }
                }
            }
        });
    }

    //paint the shape
    //initialize the white board to syc with the manager
    protected void paintComponent(Graphics g) {
        if (image == null) {
//            if (isManager) {
//                image = new BufferedImage(700, 350, BufferedImage.TYPE_INT_RGB);
//                graphics = (Graphics2D) image.getGraphics();
//                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                reset();
//            } else {
//                try {
//                    byte[] rawImage = server.sendImage();
//                    image = ImageIO.read(new ByteArrayInputStream(rawImage));
//                    graphics = (Graphics2D) image.getGraphics();
//                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                    graphics.setPaint(color);
//                } catch (IOException e) {
//                    System.out.println("Fail receiving image");
//
//                }
//            }
            image = new BufferedImage(700, 350, BufferedImage.TYPE_INT_RGB);
            graphics = (Graphics2D) image.getGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            reset();

        }
        g.drawImage(image, 0, 0, null);
    }

    public Color getCurrColor() {
        return color;
    }

    public String getCurrMode() {
        return mode;
    }

    public Graphics2D getGraphic() {
        return graphics;
    }

    public BufferedImage getCanvas() {
        saveCanvas();
        return previousCanvas;
    }

    public void reset() {
        graphics.setPaint(Color.white);
        graphics.fillRect(0, 0, 700, 350);
        graphics.setPaint(color);
        repaint();
    }

    //save image
    public void saveCanvas() {
        ColorModel cm = image.getColorModel();
        WritableRaster raster = image.copyData(null);
        previousCanvas = new BufferedImage(cm, raster, false, null);
    }

    // cover the current canvas with previous canvas states
    public void drawPreviousCanvas() {
        drawImage(previousCanvas);
    }

    public void drawImage(BufferedImage img) {
        graphics.drawImage(img, null, 0, 0);
        repaint();
    }

    // Color
    public void black() {
        this.color = Color.black;
        graphics.setPaint(color);

    }

    public void blue() {
        this.color = Color.blue;
        graphics.setPaint(color);

    }

    public void green() {
        this.color = Color.green;
        graphics.setPaint(color);

    }

    public void grey() {
        this.color = Color.gray;
        graphics.setPaint(color);

    }

    public void red() {
        this.color = Color.red;
        graphics.setPaint(color);

    }


    public void orange() {
        this.color = Color.orange;
        graphics.setPaint(color);

    }


    public void yellow() {
        this.color = Color.yellow;
        graphics.setPaint(color);

    }

    public void aoi() {
        this.color = new Color(0, 102, 102);
        graphics.setPaint(color);
    }

    public void brown() {
        this.color = new Color(153, 76, 0);
        graphics.setPaint(color);

    }

    public void cyan() {
        this.color = Color.cyan;
        graphics.setPaint(color);
    }

    public void purple() {
        this.color = new Color(102, 0, 204);
        graphics.setPaint(color);
    }

    public void pink() {
        this.color = new Color(255, 153, 204);
        graphics.setPaint(color);

    }

    public void magenta() {
        this.color = Color.magenta;
        graphics.setPaint(color);
    }

    public void sky() {
        color = new Color(0, 128, 255);
        graphics.setPaint(color);

    }

    public void darkGrey() {
        this.color = Color.darkGray;
        graphics.setPaint(color);

    }

    public void lime() {
        this.color = new Color(102, 102, 0);
        graphics.setPaint(color);

    }

    // mode
    public void draw() {
        mode = "draw";
    }

    public void line() {
        mode = "line";
    }

    public void circle() {
        mode = "circle";
    }

    public void rectangle() {
        mode = "rectangle";
    }

    public void oval() {
        mode = "oval";
    }

    public void triangle() {
        mode = "triangle";
    }

    public void text() {
        mode = "text";
    }

    public void eraser() {
        mode = "eraser";
    }


    public Shape makeLine(Shape shape, Point start, Point end) {
        shape = new Line2D.Double(start.x, start.y, end.x, end.y);
        return shape;
    }

    public Shape makeCircle(Shape shape, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Ellipse2D.Double(x, y, Math.max(width, height), Math.max(width, height));
        return shape;
    }


    public Shape makeRectangle(Shape shape, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Rectangle2D.Double(x, y, width, height);
        return shape;
    }


    public Shape makeText(Shape shape, Point start) {
        int x = start.x - 5;
        int y = start.y - 20;
        int width = 130;
        int height = 25;
        shape = new RoundRectangle2D.Double(x, y, width, height, 15, 15);
        return shape;
    }

    public Shape makeOval(Shape shape, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Ellipse2D.Double(x, y, width, height);
        return shape;
    }

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
}



