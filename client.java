import java.net.*;
import java.io.*;

public class client{
    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4999);
        System.out.print("\033[H\033[2J");

        if (s.isConnected()){
            System.out.println("connected to server successfully");
        }


        //SENDING
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        System.out.println("client : " + "its me - the client");
        pr.println("its me - the client");
        pr.flush();


        //RECEIVING
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server : " + str);

    }


}