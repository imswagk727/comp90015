package Server;

import Remote.CanvasClientInterface;
import Remote.CanvasMsgInterface;
import Remote.CanvasServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

// servant class
public class CanvasServer extends UnicastRemoteObject implements CanvasServerInterface {

    private static final long serialVersionUID  = 1L;
    private ClientManager clientManager;

    // CanvasServer contains a client manager that manages the client
    // The server receives client registrations and serve as the communication channel between clients
    public CanvasServer() throws RemoteException {
        this.clientManager = new ClientManager(this);
    }


    @Override
    public void register(CanvasClientInterface client) throws RemoteException {
        // first user -> manager
        if (this.clientManager.isEmpty()) {
            client.assignManager();
        }

        boolean permission = true;
        // ask permission to join
        for (CanvasClientInterface c : this.clientManager) {
            if (c.getManager()) {
                try {
                    permission = c.askPermission(client.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        // set the client's permission
        if (!permission) {
            try {
                client.setPermission(permission);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        // manager marks
        if (client.getManager()) {
            client.setName("*" + client.getName());
        }

        // add client to client manager
        this.clientManager.addClient(client);

        // update ClientList
        for (CanvasClientInterface c : this.clientManager) {
            c.updateUserList(getClientList());
        }
    }

    // broadcast
    @Override
    public void broadCastCanvas(CanvasMsgInterface message) throws RemoteException {
        for (CanvasClientInterface c : this.clientManager) {
            c.syncCanvas(message);
        }
    }

    // if manager pressed new, let all clients clear Canvas
    @Override
    public void refreshCanvas() throws RemoteException {
        for (CanvasClientInterface c : this.clientManager) {
            c.refreshCanvas();
        }
    }

    @Override
    public Set<CanvasClientInterface> getClientList() throws RemoteException {
        return this.clientManager.getClientList();
    }


    // return manager's canvas
    public byte[] sendImage() throws IOException {
        byte[] currentImage = null;
        for (CanvasClientInterface c : this.clientManager) {
            if (c.getManager()) {
                currentImage = c.sendImage();
            }
        }
        return currentImage;
    }

    @Override
    public void sendOpenedImage(byte[] rawImage) throws IOException {
        for (CanvasClientInterface c : this.clientManager) {
            if (!c.getManager()) {
                c.drawOpenedImage(rawImage);
            }
        }
    }

    @Override
    public void removeMe(String name) throws RemoteException {
        for (CanvasClientInterface c : this.clientManager) {
            if (c.getName().equals(name)) {
                this.clientManager.deleteClient(c);
                System.out.println(name + "has left");
            }
        }

        //update each client's userList
        for (CanvasClientInterface c : this.clientManager) {
            c.updateUserList(getClientList());
        }
    }

    @Override
    public void deleteClient(String name) throws IOException {
        System.out.println(name + "has been kicked off");
        for (CanvasClientInterface c : this.clientManager) {
            if (c.getName().equals(name)) {
                this.clientManager.deleteClient(c);
                c.closeUI();
            }
        }
    }

    @Override
    public void removeAll() throws IOException {
        System.out.println("Manager has left");
        for (CanvasClientInterface c : this.clientManager) {
            this.clientManager.deleteClient(c);
            c.closeUI();
        }
    }

    @Override
    public void addChat(String text) throws RemoteException {
        for (CanvasClientInterface c : this.clientManager) {
            try {
                c.addChat(text);
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
}
