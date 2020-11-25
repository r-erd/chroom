import java.net.*;
import java.io.*;
import java.awt.* ;
import java.awt.image.BufferedImage;

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
           System.exit(0);
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }


    public void execute() {
        try {
            System.out.print("\033[H\033[2J");  // von Stackoverflow genommen, cleared console, geht nicht auf Windows?!
            System.out.println("successfully connected to the server\n\navailable commands:\n!quit = disconnect from the server\n!connection = check your connection\n!online = get a list of online users\n");
            Ascii("chatroom", 12); // von Stackoveflow genommen
            Socket socket = new Socket(hostname, port);
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException e){
            System.out.println("couldn't connect to server: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    void setUsername(String userName) {
            this.userName = userName;
    }
   
    String getUsername() {
            return this.userName;
    }

    public void Ascii(String text, int font){

            int width = 100;
            int height = 30;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setFont(new Font("SansSerif", Font.BOLD, font));
    
            Graphics2D graphics = (Graphics2D) g;
            graphics.drawString(text, 10, 20);
    
           //save this image
           //ImageIO.write(image, "png", new File("/users/mkyong/ascii-art.png"));
    
            for (int y = 0; y < height; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    sb.append(image.getRGB(x, y) == -16777216 ? " " : "$");
                }
                if (sb.toString().trim().isEmpty()) {
                    continue;
                }
    
                System.out.println(sb);
            }
        }
}