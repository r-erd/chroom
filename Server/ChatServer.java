import java.net.*;    //Internetzugriff
import java.io.*;     //Internet i/o
import java.util.*;   //Has 


public class ChatServer{

        private int port;
        
        //use sets to keep track of users and their respective threads, sets don't allow duplicates
        private Set<String> userNames = new HashSet<>(); //brauchen wir gar nicht mehr


        private Set<User> database = new HashSet<User>();   //speichert User und deren Passwort
        private Set<UserThread> userThreads = new HashSet<>();  //darin werden die Threads verwaltet

        public ChatServer(int port) {
            this.port = port;
        }

        public void execute() {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                //database.add(new User("root", "root", false));
                System.out.println("Chat Server is listening on port " + this.port);

                while (true) {
                    Socket socket = serverSocket.accept(); //Serverstart
                    System.out.println("someone connected");

                    //erstelle eigenen Thread für den User auf dem Server und starte ihn
                    UserThread newUser = new UserThread(socket, this);
                    userThreads.add(newUser);
                    newUser.start();
                }

            } catch (IOException e) {
                System.out.println("Error in the server: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static void main(String[] args) { //Port als String
            if (args.length < 1) {
                System.out.println("start application like this: java ChatServer <port-number>");
                System.exit(0);
            }
            
            int port = Integer.parseInt(args[0]); //wandelt String zu Integer um (Port)

            ChatServer server = new ChatServer(port);
            server.execute();
        }

        ////////////////////////////////////// MESSAGING  //////////////////////////////////////////7

        //Message schreiben (bekommen alle, nur Sender nicht)
        void transmit(String message, UserThread excludeUser) {
            for (UserThread aUser : userThreads) {
                if (aUser != excludeUser) {
                    if (aUser.loggedIn) {
                        aUser.sendMessage(message);
                    }
                }
            }
        }

        //Nachricht an bestimmten User
        void transmitSingle(String message, UserThread user) {
             user.sendMessage(message);
        }


        ////////////////////// USER MANAGEMENT //////////////////////////////////////7

        public void addUser(String username, String password){
            this.database.add(new User(username, password, true));  //true setzt User auf Online --> er wird unter "connected Users" angezeigt
        }

        //QUIT CHAT - removes username and userthread
        void removeUser(String userName, UserThread aUser) {
            this.setOffline(userName);
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " disconnected");
        }
    
        public void changePassword(String username,String oldpassword, String newpassword){
            for (User aUser : database){
                if (this.checkPassword(username, oldpassword)){
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

        public boolean checkOnline(String username){
            for (User aUser : database){
                if (aUser.username.equals(username)){
                    if (aUser.online == true)
                        return true;
                }
            }
            return false;
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
            return users;  //returned sowas : jonas robin fredda
        }


        //vll obsolet
        boolean hasUsers(){
            return !this.database.isEmpty();
        }   
}