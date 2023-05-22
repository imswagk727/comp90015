/*
 * Shuangkun Fan (1131667)
 *
 * Server interface of RMI
 * contains method to be revoked by client (client远程调用）
 *
 */

package Remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface CanvasServerInterface extends Remote {

    //register the clients connecting to our server
    public void register(CanvasClientInterface client) throws RemoteException;


    // boardcasting drawing
    public void broadCastingCanvas(CanvasMsgInterface message) throws  RemoteException;

    // all clients clear canvas
    public void refreshCanvas() throws RemoteException;

    // get list of users
    public Set<CanvasClientInterface> getClientList() throws RemoteException;

    // for new user, show current board to them
    public byte[] sendImage() throws IOException;

    // when opening a image, show them for all user (manager)
    public void sendOpenedImage(byte[] rawImage) throws IOException;

    // refresh user list when a user leaving
    public void removeMe(String name) throws RemoteException;

    // kick off user (manager)
    public void deleteClient(String selectName) throws IOException;

    // remove all user (manager leaving)
    public void removeAll() throws IOException;

    // show chat history
    public void addChat(String text) throws RemoteException;


}
