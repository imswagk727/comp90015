package client;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Shuangkun Fan (1131667)
 */
public class DictionaryClient {
    static Socket client = null;
    static BufferedReader reader;
    static BufferedWriter writer;


    public static void main(String[] args) throws IOException {
        //multi-threaded
        // new thread for ClientUI
        ClientUI clientUI = new ClientUI();
        clientUI.start();
//        int port = Integer.parseInt(args[1]); // for assignment

        try {
            client = new Socket("localhost", 4444); // for testing
//            client = new Socket(args[0], port); // for assignment
        } catch (ConnectException ce) {
            JOptionPane.showMessageDialog(null, "Wrong port number or server connection error");
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "UnknownHostException Error");
        } catch (IOException e) {
            System.out.println("IOException error");
        }

        while (true) {
            try {
                // IO encapuliaton
                reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

                //word's meaning or action's outcome
                String msgFromServer = reader.readLine();
                String que = "QUERYmessage";
                if (msgFromServer.length() > que.length() && msgFromServer.substring(0, que.length()).equals(que)) {
                    String[] arrayRec = msgFromServer.split("/");
                    ClientUI.textAreaMeaning.setText(arrayRec[1]);
                } else {
                    ClientUI.textMsg.setText(msgFromServer);
                }


            } catch (SocketException e) {
                //what about client if server closed?
                JOptionPane.showMessageDialog(null, "lost connection from the server");
                if (client != null) {
                    client.close();
                }
                clientUI.interrupt();
                return;
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "lost connection from the server");
                clientUI.interrupt();
                return;
            }
        }

    }
}
