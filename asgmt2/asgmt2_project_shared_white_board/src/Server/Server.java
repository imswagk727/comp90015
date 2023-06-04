package Server;

import Remote.CanvasServerInterface;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            CanvasServerInterface server = new CanvasServer();
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt("1234")); // testing
//            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0])); // submission
            registry.bind("WhiteBoardServer", server);
            JOptionPane.showMessageDialog(null, "Server is ready!");
        } catch (Exception e){
            System.out.println("not valid port number");
            e.printStackTrace();
            return;
        }
    }
}
