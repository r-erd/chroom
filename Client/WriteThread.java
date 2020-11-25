import java.io.*;
import java.net.*;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
    
        // ======================== ESTABLISH CONNECTION =================================

        try {
            OutputStream output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //==================================== LOG IN =================================

    public void run() {
        Console console = System.console();  //doesnt work in intelliJ
        String userName = console.readLine("\nEnter your username: ");
        client.setUsername(userName);
        this.writer.println(userName);

        if (client.userExists)  {  // user exists
                    //enter password for existing user or create new password for new user
        do {
            String password = console.readLine("\n");
            this.writer.println(password);
            //check if password is correct 
            ///get message from server whether password is correct

            } while (!client.loggedIn);
        } else {  //user doesnt exist
                String password = console.readLine("\n"); //set new password
                this.writer.println(password);                 //and transmit it to server to save it!
        }

        // ===================== GET AND SEND MESSAGES ===================================
        String text;

        do {
            text = console.readLine();
            this.writer.println(text);
        } while (!text.equals("!quit"));

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error writing to server: " + e.getMessage());
            e.printStackTrace();
        }
    }




}

