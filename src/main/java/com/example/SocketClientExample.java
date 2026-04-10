package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 


public class SocketClientExample {
   
   
    /*
     * Modify this example so that it opens a dialogue window using java swing,
     * takes in a user message and sends it
     * to the server. The server should output the message back to all connected clients
     * (you should see your own message pop up in your client as well when you send it!).
     *  We will build on this project in the future to make a full fledged server based game,
     *  so make sure you can read your code later! Use good programming practices.
     *  ****HINT**** you may wish to have a thread be in charge of sending information
     *  and another thread in charge of receiving information.
    */
    // Precondition: None
    // Postcondition: GUI displayed, connected to server on port 9876, listening for messages
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        JFrame frame = new JFrame("TextBoxes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLayout(new FlowLayout()); 
        JTextField textField1 = new JTextField(100); 
        JTextField textField2 = new JTextField(100);
        frame.add(textField1);
        frame.add(textField2);
        frame.setVisible(true);
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream   oos = new ObjectOutputStream(socket.getOutputStream());
        Scanner input = new Scanner(System.in);
        textField1.addActionListener(new ActionListener() {

            // Precondition: e is a valid ActionEvent
            // Postcondition: Text from textField1 sent to server via oos
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String line = textField1.getText();
                try {
                    oos.writeObject(line);
                    oos.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            
            }
            
        });
        while(true){
            textField2.setText(textField2.getText()+"\n\n"+(String) ois.readObject());
        }

    }
}