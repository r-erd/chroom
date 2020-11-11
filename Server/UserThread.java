import java.net.*;
import java.io.*;
import java.util.*;

public class UserThread extends Thread { //handles the connection for each connected client, server starts one thread per user

    private Socket s; //ist quasi die Verbindung zum Client bzw die Leitung port usw
    private ChatServer server;
    private PrintWriter writer;
    private boolean loggedIn;

    public UserThread(Socket s, ChatServer server) {  //übergibt Referenz auf Server und Socket (Verbindung)
            this.s = s;
            this.server = server;
    }

    public void run() {
        try {
            ///////////////// ESTABLISH CONNECTION ///////////////////////////
            InputStream in = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    
            OutputStream out = s.getOutputStream();
            this.writer = new PrintWriter(out, true); //auto-flush enabled

            writer.println("currently connected users: " + server.getOnlineUsers());  //make this return an array and print it with the number of onlineUsers

            /////////////////////////////////////////////////////// LOG IN ////////////////////////////////
            String userName = reader.readLine();


            if (server.userExists(userName)){                  //in bestehenden Account einloggen
                do {
                    server.transmitSingle("\n[server] : You already have an account. Please enter correct password.", this);
                    String password = reader.readLine();
                    if (server.checkPassword(userName, password)){
                        this.loggedIn = true;
                    }
                    } while (!this.loggedIn);
            } else {                                            //neuen Account erstellen 
                server.transmitSingle("\n[server] : You are a new user. Please set a password.", this);
                String password = reader.readLine(); //empfange Passwort
                server.addUser(userName, password);
                this.loggedIn = true;
            }


            server.transmitSingle("\n[server] : You are now logged in! \n", this);  //only to the client
            server.transmit("\n[server] : " +  userName + " connected", this);      //to all the others
            server.setStatus(userName, true);                                       //set online status

            String clientMessage;
            String serverMessage;

            //////// LISTEN FOR MESSAGES ////////////////////////////
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "] : " + clientMessage;
                server.transmit(serverMessage, this); 
            } while (!clientMessage.equals("!quit"));

            ////////// END CONNECTION /////////////////
            server.removeUser(userName, this);
            s.close();
            serverMessage = userName + " disconnected.";
            server.transmit(serverMessage, this);  
        } catch (IOException e) {
            System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }


            //==========================================================================================




            //SEND MESSAGE TO SERVER (write to Output Stream)
    public void sendMessage(String message){
        writer.println(message);
    }

    public boolean authenticated(){
        return this.loggedIn;
    }
}