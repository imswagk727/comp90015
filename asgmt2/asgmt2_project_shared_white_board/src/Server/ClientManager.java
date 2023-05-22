package Server;

import Remote.CanvasClientInterface;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager implements Iterable<CanvasClientInterface>{

    // store clients list
    private Set<CanvasClientInterface> clientList;

    // constructor
    public ClientManager(CanvasServer canvasServer) {
        this.clientList = Collections.newSetFromMap(new ConcurrentHashMap<CanvasClientInterface, Boolean>());
    }

    public void addClient(CanvasClientInterface client) {
        this.clientList.add(client);
    }

    public Set<CanvasClientInterface> getClientList(){
        return this.clientList;
    }

    //check if clientList is empty (no client)
    public boolean isEmpty() {
        return false;
    }

    // remove a client from ClientList
    public void deleteClient(CanvasClientInterface client){
        this.clientList.remove(client);
    }

    // make clientList iterable
    @Override
    public Iterator<CanvasClientInterface> iterator() {
        return clientList.iterator();
    }
}
