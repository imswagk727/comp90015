/*
 * Shuangkun Fan (1131667)
 *
 * communication message Interface
 */

package Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CanvasMsgInterface extends Remote {

    // get for message wrapper
    public String getState() throws RemoteException;

    public String getName() throws RemoteException;
    public String getMode() throws RemoteException;
    public String getColor() throws RemoteException;
    public String getPoint() throws RemoteException;
    public String getText() throws RemoteException;

}
