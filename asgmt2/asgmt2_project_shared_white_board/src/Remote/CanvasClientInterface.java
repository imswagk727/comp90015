/*
 * Shuangkun Fan (1131667)
 *
 * Client interface of RMI （server 远程调用）
 * contains the method which invoked by server's clientManager
 */

package Remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface CanvasClientInterface extends Remote {

    // calls the client UI
    public void drawBoard (CanvasServerInterface server) throws RemoteException;

    // update userList for Whiteboard UI
    public void updateUserList(Set<CanvasClientInterface> list) throws  RemoteException;

    // sync whiteboard with every user
    public void syncCanvas(CanvasMsgInterface msg) throws RemoteException;

    // get client name
    public String getName() throws RemoteException;

    // set client name
    public void setName(String name) throws RemoteException;

    // get manager
    public boolean getManager() throws RemoteException;

    // set manager
    public void setManager() throws RemoteException;

    // clear whiteboard
    public void refreshCanvas() throws RemoteException;

    //send the whiteboard as image to server
    public byte[] sendImage() throws RemoteException;

    // draw on opened image
    public void drawOpenedImage(byte[] rawImage) throws IOException;

    // chat
    public void addChat(String text) throws RemoteException;

    // permission
    public boolean askPermission(String name) throws IOException;

    public boolean getPermission() throws IOException;

    public void setPermission(boolean permission) throws IOException;


    // close UI
    public void closeUI() throws IOException;



}
