package com.example;
import java.net.*;
import java.awt.FlowLayout;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example).
 *
 * This version of the program creates a new thread for
 * every connection request.
 */
public class ChatServerWithThreads {

    public static final int LISTENING_PORT = 9876;

    // Precondition: None
    // Postcondition: Server listens on port 9876 or shuts down on error
    public static void main(String[] args) {
        
        // JFrame frame = new JFrame("TextBoxes");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(300, 100);
        // frame.setLayout(new FlowLayout()); 
        // JTextField textField1 = new JTextField(100); 
        // JTextField textField2 = new JTextField(100);
        // frame.add(textField1);
        // frame.add(textField2);
        // frame.setVisible(true);

        ServerSocket listener;  // Listens for incoming connections.
        Socket connection;      // For communication with the connecting program.

        /* Accept and process connections forever, or until some error occurs. */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                // Accept next connection request and handle it.
                ConnectionHandler h = new ConnectionHandler(connection);
                h.start();
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()


    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */
    private static class ConnectionHandler extends Thread {
        private static volatile ArrayList<ConnectionHandler> handlers; //This is shared among all the connection handlers
        Socket client;
        ObjectOutputStream oos; //you'll need to define this one for when you're ready to talk back to the client!
        ObjectInputStream ois;
        // Precondition: socket is a valid Socket object
        // Postcondition: ConnectionHandler initialized with socket, streams set up, added to handlers
        ConnectionHandler(Socket socket) {
            client = socket;
   
            if(handlers== null){
                handlers = new ArrayList();
            }
            handlers.add(this);
            try{
            oos = new ObjectOutputStream(client.getOutputStream());
            ois = new ObjectInputStream(client.getInputStream());
            }
            catch(Exception e){}
        }
        // Precondition: ConnectionHandler is properly initialized
        // Postcondition: Handles client messages until disconnect or error occurs
        public void run() {
            String clientAddress = client.getInetAddress().toString();
            while(true) {
                try {
                    String message = (String)ois.readObject();
                    if(!message.equals("disconnect")){
                        System.out.println(message);
                    }
                    else{
                        System.out.println("closing connection");
                        break;
                       
                    }
                    for(ConnectionHandler h: handlers){
                        h.oos.writeObject(message);
                        h.oos.flush();
                    }
                }
                catch(EOFException e){
                    System.out.println("the client disconnected, bye!!!");
                    handlers.remove(this);
                    break;
                }
                catch (Exception e){
                    System.out.println("Error on connection with: "
                            + clientAddress + ": " + e);
                }
            }
        }
    }


}