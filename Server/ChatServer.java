import java.net.*;
import java.io.*;
import java.util.*;


public class ChatServer{

        private int port;
        //use sets to keep track of users and their respective threads, sets don't allow duplicates
        private Set<String> userNames = new HashSet<>();
        private Set<User> database = new HashSet<User>();
        private Set<UserThread> userThreads = new HashSet<>();

        public ChatServer(int port) {
            this.port = port;
        }

        public void execute() {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                database.add(new User("root", "root", false));
                System.out.println("Chat Server is listening on port " + this.port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("someone connected");

                    UserThread newUser = new UserThread(socket, this);
                    userThreads.add(newUser);
                    newUser.start();
                }

            } catch (IOException e) {
                System.out.println("Error in the server: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            if (args.length < 1) {
                System.out.println("start application like this: java ChatServer <port-number>");
                System.exit(0);
            }



            int port = Integer.parseInt(args[0]);

            ChatServer server = new ChatServer(port);
            server.execute();
        }

        ////////////////////////////////////// managing  stuff //////////////////////////////////////////7



        void transmit(String message, UserThread excludeUser) {
            for (UserThread aUser : userThreads) {
                if (aUser != excludeUser) {
                    aUser.sendMessage(message);
                }
            }
        }

        void transmitSingle(String message, UserThread user) {
             user.sendMessage(message);
        }

        //QUIT CHAT - removes username and userthread
        void removeUser(String userName, UserThread aUser) {
            this.setOffline(userName);
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " disconnected");
        }

        


 

        ////////////////////// USER MANAGEMENT //////////////////////////////////////7

        public void addUser(String username, String password){
            this.database.add(new User(username, password, true));
        }
    
        public void changePassword(String username,String oldpassword, String newpassword){
            for (User aUser : database){
                if (checkPassword(username, oldpassword)){
                    aUser.password = newpassword;
                }
            }
            this.database.add(new User(username, newpassword, true));
        }
    
        public boolean userExists(String username){
            for (User aUser : this.database){
                if (aUser.username.equals(username)){
                    return true;
                }
            }

            return false;
        }

        public void setOnline(String username){
            for (User aUser : database){
                if (aUser.username.equals(username)){
                    aUser.online = true;
                }
            }
        }

        public void setOffline(String username){
            for (User aUser : database){
                if (aUser.username.equals(username)){
                    aUser.online = false;
                }
            }
        }
    
        public boolean checkPassword(String username, String password){
            for (User aUser : database){
                if (aUser.username.equals(username) && aUser.password.equals(password)){
                    return true;
                }
            }
            return false;
        }

        public String getOnlineUsers(){
            String users = "";
            for (User aUser : database){
                if (aUser.online == true){
                    users += " " + aUser.username;
                }
            }
            return users;
        }

        boolean hasUsers(){
            return !this.database.isEmpty();
        }







/*         ServerSocket ss = new ServerSocket(4999);
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
        pr.flush(); */
    
    
}