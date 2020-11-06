import java.net.*;
import java.io.*;
import java.util.*;

public class UserThread { //handles the connection for each connected client, server starts one thread per user

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
            this.socket = socket;
            this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUsername(userName);

            String serverMessage = "New user connected: " + userName;
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

    //tools
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }


    //actually receives the messages by being called in the server for this thread with the message??
    void sendMessage(String message){
        writer.println(message);
    }














/*     public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 4999);
        System.out.print("\033[H\033[2J");

        if (socket.isConnected()){
            System.out.println("connected to server successfully");
        }

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();


        //SENDING
        PrintWriter pr = new PrintWriter(outputStream);
        System.out.println("client : " + "its me - the client");
        pr.println("its me - the client");
        pr.flush();
        

        //RECEIVING
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server : " + str);
    } */


}