package server;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shuangkun Fan (1131667)
 */
public class DictionaryServer {
    static String fileName;
    //ConcurrentHashMap for concurrency control
    private static final ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<String, String>();
    static List<Socket> connectionList = new ArrayList<>();
    static int i = 0;
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket request = null;

//        String port = args[0];
//        fileName = args[1];
        try {
            server = new ServerSocket(4444);
            fileName = "dictionary.txt";
            i = 0;
        } catch (BindException e) {
            System.out.println("Port number address already in use, Pick another one");
        } catch (IOException e) {
            System.out.println("IOException error");
        }

        ServerUI sui = new ServerUI();
        sui.start();
        //save dictionary
        try {
            readDictionary();
        } catch (IOException e) {
            System.err.println("An I/O error occurred while reading the dictionary file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while reading the dictionary file: " + e.getMessage());
        }


        //Thread per connection
        while (true) {
            try {
                System.out.println("Server listening on port" + " " + "for a connection");
                request = server.accept();
                connectionList.add(request);
                i++;
                System.out.println("Client conection number " + i + " accepted:");
                ServerUI.lblConnectionNum.setText(String.valueOf(i));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            //create connection and thread
            try {
                new ServerThreadConnection(request, dictionary).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void readDictionary() throws IOException {
        /**
         * read the dictionary file and put it to hashmap
         */
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;

            try {
                inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);
            } catch (Exception e) {
                System.out.println("File not found or unsupported file encoding type");
            }

            //save file content to hashmap
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lines = line.split(":");
                dictionary.put(lines[0], lines[1]);
            }
            inputStreamReader.close();
            bufferedReader.close();
        }
    }


    public static void writeDictionary() {
        /**
         * save the dictionary Hashmap to the file
         */
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Entry<String, String> entry : dictionary.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("cannot save the dictionary Hashmap to the file");
        }
//        try {
//            FileWriter writer = new FileWriter(fileName);
//            for (Map.Entry<String, String> entry : dictionary.entrySet()) {
//                //write key-valued pair to text file0
//                String key = entry.getKey();
//                String value = entry.getValue();
//                writer.write(key + ":" + value + "\n");
//            }
//            writer.close();
//        } catch (IOException e) {
//            System.err.println("An error occurred while writing the dictionary to file: " + e.getMessage());
//        }

    }
}
