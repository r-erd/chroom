import java.io.*;
import java.net.*;

//reads messages from the server and prints them

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
        }


        //login protocol here???!




    }


public void run() {
    while(true){
        try {
    

            String response = reader.readLine();
            System.out.println(response);
            


            if (response.equals("\n [server] : You are now logged in!")){
                client.loggedIn = true;
            }

            if (response.equals("\n [server] : You already have an account. Please enter correct password.")){
                client.userExists = true;
            }

            //print username after displaying the message
/*             if (client.getUsername() != null) {
                System.out.print("[" + client.getUsername() + "]:");
            } */

        } catch (IOException e){
            System.out.println("Error reading from server: " + e.getMessage());
            e.printStackTrace();
            break;
        }
    }
}

}