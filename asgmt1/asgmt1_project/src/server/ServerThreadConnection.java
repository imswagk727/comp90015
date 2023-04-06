package server;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThreadConnection extends Thread {

    Socket socket = null;
    ConcurrentHashMap<String, String> dictionary;
    BufferedReader input = null;
    BufferedWriter output = null;


    @Override
    public void run() {
        //interaction with ClientUI
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //from CientUI
        String request;
        while (true) {
            try {
                request = input.readLine();
                if (request == null) {
                    socket.close();
                    return;
                } else {
                    String[] requestArr = request.split("/");
                    String requestType = requestArr[0]; // add remove update query
                    String word = requestArr[1];
                    String meaning = null; //query has no meaning
                    if (requestArr.length == 3) {
                        meaning = requestArr[2];
                    }
//                    if (requestType.equals("ADD")) {
//                        if (dictionary.get(word) != null) {
//                            output.write(word + "already exists!\n");
//                            output.flush();
//                        } else {
//
//                            dictionary.put(word, meaning);
//                            DictionaryServer.writeDictionary();
//                            output.write(word + "has been added");
//                            output.flush();
//                        }
//                    }
                    switch (requestType) {
                        case "ADD":
                            if (dictionary.get(word) != null) {
                                try {
//                                    output.write("'" + word + "' already exists!\n");
                                    JOptionPane.showMessageDialog(null, "'" + word + "' already exists!");
                                    output.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            dictionary.put(word, meaning);
                            try {
                                DictionaryServer.writeDictionary();
//                                output.write("'" + word + "' has been added!\n");
                                JOptionPane.showMessageDialog(null, "'" + word + "' has been added!");
                                output.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case "REMOVE":
                            if (dictionary.get(word) == null) {
                                try {
//                                    output.write("Cannot remove, word not found in the dictionary!\n");
                                    JOptionPane.showMessageDialog(null, "Cannot remove, word not found in the dictionary!");
                                    output.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            // word is in the dictionary
                            dictionary.remove(word);
                            try {
                                DictionaryServer.writeDictionary();
//                                output.write("'" + word + "' has been removed!\n");
                                JOptionPane.showMessageDialog(null, "'" + word + "' has been removed!");
                                output.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;


                        case "UPDATE":
                            // word is not in the dictionary
                            // word is in the dictionary (user provide same meaning / different meaning)
                            if (dictionary.get(word) == null) {
                                try {
//                                    output.write("Cannot update, word not found in the dictionary!\n");
                                    JOptionPane.showMessageDialog(null, "Cannot update, word not found in the dictionary!");
                                    output.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            } else if (dictionary.get(word).equals(meaning)) {
                                try {
//                                    output.write("Your provided the same meaning!\n");
                                    JOptionPane.showMessageDialog(null, "Cannot update, Your provided the same meaning!");
                                    output.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            dictionary.remove(word);
                            dictionary.put(word, meaning);
                            try {
                                DictionaryServer.writeDictionary();
//                                output.write("The meaning of " + "'" + word + "' has been updated!\n");
                                JOptionPane.showMessageDialog(null, "The meaning of " + "'" + word + "' has been updated!");

                                output.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case "QUERY":
                            // word is not in dictionary
                            if (dictionary.get(word) == null) {
                                try {
//                                    output.write("word not found in the dictionary!\n");
                                    JOptionPane.showMessageDialog(null, "word not found in the dictionary!");
                                    output.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            // word is in the dictionary
                            String qMeaning = dictionary.get(word);
                            try {
                                output.write("QUERYmessage" + "/" + qMeaning + "\n");
                                output.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                            break;
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


    }

    public ServerThreadConnection(Socket socket, ConcurrentHashMap<String, String> dictionary) throws IOException {
        this.socket = socket;
        this.dictionary = dictionary;
    }
}
