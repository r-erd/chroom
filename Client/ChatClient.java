import java.net.*;
import java.io.*;

public class ChatClient {
    private String hostname;
    private int port;
    public String userName;
    public boolean loggedIn;
    public boolean userExists;
    public boolean correctPassword;
    
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }


    public static void main(String[] args){
        if(args.length < 2){ 
           System.out.println("invalid synatax, start Client like this:  java ChatClient [ip] [port]");
           return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }


    public void execute() {
        try {
            System.out.print("\033[H\033[2J");  // von Stackoverflow genommen, cleared console
            System.out.println("connected to the server \n\navailable commands:\n!quit = disconnect from the server  \n!connection = check your connection by sending  \n!online = get a list of online users\n");

            Socket socket = new Socket(hostname, port);
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException e){
            System.out.println("couldn't connect to server: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
    
         void setUsername(String userName) {
            this.userName = userName;
        }
   
        String getUsername() {
            return this.userName;
        }
}