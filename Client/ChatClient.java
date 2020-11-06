package chat.client;

import java.net.*;
import java.io.*;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    

    //constructor
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("connected to the server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException e){
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

    }

    //tools
     void setUsername(String userName) {
         this.userName = userName;
     }

     String getUsername() {
         return this.userName;
     }

     public static void main(String[] args){
         if(args.length < 2){ 
            System.out.println("invalid hostname!");
            return;
         }

         String hostname = args[0];
         int port = Integer.parseInt(args[1]);

         ChatClient client = new ChatClient(hostname, port);
         client.execute();
     }
        

    
}