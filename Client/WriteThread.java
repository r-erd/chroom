import java.io.*;
import java.net.*;

//reads users input and sends it to the server

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
    

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        Console console = System.console();
        
        String userName = console.readLine("\nEnter your username: ");
        client.setUsername(userName);
        writer.println(userName);


        //check input password or save password to server
        //wait for success message from server (changes loggedIn boolean)


//========================================================== user exists
        if (client.userExists)  { 


                    //enter password for existing user or create new password for new user
        do {
            String password = console.readLine("\nEnter your password: ");
            writer.println(password);

            } while (!client.loggedIn);
            //check if password is correct 
            ///get message from server whether password is correct
            

        } else {  // =====================================================    user doesnt exist
            
                //enter new password 
                String password = console.readLine("\nEnter your password: ");
                writer.println(password);
                //and transmit it to server to save it!


        }

    






        String text;

        do {
            text = console.readLine();
            writer.println(text);
        } while (!text.equals("!quit"));

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error writing to server: " + e.getMessage());
            e.printStackTrace();
        }
    }




}

