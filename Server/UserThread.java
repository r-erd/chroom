import java.net.*;
import java.io.*;
import java.util.*;

public class UserThread extends Thread { //handles the connection for each connected client, server starts one thread per user

    private Socket socket; //ist quasi die Verbindung zum Client bzw die Leitung port usw
    private ChatServer server;
    private PrintWriter writer;
    public boolean loggedIn;


    public UserThread(Socket socket, ChatServer server) {  //übergibt Referenz auf Server und Socket (Verbindung)
            this.socket = socket;
            this.server = server;
    }


    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);

            writer.println("currently connected users: " + server.getOnlineUsers());

            /////////////////////////////////////////////////////// LOG IN ////////////////////////////////


            String userName = reader.readLine(); //empfange Username

                //user exists 
            if (server.userExists(userName)){
                do {
                    server.transmitSingle("\n[server] : You already have an account. Please enter correct password.", this);
                    String password = reader.readLine();
                    if (server.checkPassword(userName, password)){
                        this.loggedIn = true;
                        //we dont ever get here
                    }
                    } while (!this.loggedIn);


            } else {
                server.transmitSingle("\n[server] : You are a new user. Please set a password.", this);
                //create new user
                String password = reader.readLine(); //empfange Passwort
                server.addUser(userName, password);
                this.loggedIn = true;
            }

            server.transmitSingle("\n[server] : You are now logged in! \n", this);

            server.setOnline(userName);

            /////////////////////////////////////////// regular message sending and receiving //////////////////////////////////




            String serverMessage = "\n[server] : " +  userName + " connected";
            server.transmit(serverMessage, this);

            String clientMessage;

            do {
                //do stuff while connected to the server
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "] : " + clientMessage;
                server.transmit(serverMessage, this); 

            } while (!clientMessage.equals("!quit"));

            server.removeUser(userName, this);
            socket.close();
             
            serverMessage = userName + " disconnected.";
            server.transmit(serverMessage, this);  

        } catch (IOException e) {
            System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////// helping functions ////////////////////////////////////////

    //actually receives the messages by being called in the server for this thread with the message??
    void sendMessage(String message){
        writer.println(message);
    }

}