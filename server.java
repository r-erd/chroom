import java.net.*;
import java.io.*;

public class server{
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(4999);
        System.out.print("\033[H\033[2J");
        Socket s = ss.accept();

        System.out.println("client connected successfully");

        //RECEIVING
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("client : " + str);

        //SENDING
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        System.out.println("server : " + "its me - the server");
        pr.println("it's me - the server");
        pr.flush();
    
    }
}