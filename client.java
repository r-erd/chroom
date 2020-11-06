import java.net.*;
import java.io.*;

public class client{
    public static void main(String[] args) throws IOException {

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

    }


}