package chat.client;

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

        String text;

        do {
            text = console.readLine("[" + userName + "]: ");
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

