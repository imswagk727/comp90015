package Client;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import Remote.CanvasMsgInterface;

public class MessageWrapper extends UnicastRemoteObject implements CanvasMsgInterface{
    private static final long serialVersionUID = 1L;
    private String drawState;
    private String clientName;
    private String mode;
    private Color color;
    private Point point;
    private String text;

    public MessageWrapper(String state, String name, String mode, Color color, Point point, String text) throws RemoteException{
        this.drawState = state;
        this.clientName = name;
        this.mode = mode;
        this.color = color;
        this.point = point;
        this.text = text;

    }

    @Override
    public String getState() {
        return this.drawState;
    }

    @Override
    public String getName() {
        return this.clientName;
    }

    @Override
    public String getMode() {
        return this.mode;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Point getPoint() {
        return this.point;
    }

    @Override
    public String getText() {
        return this.text;
    }

}
