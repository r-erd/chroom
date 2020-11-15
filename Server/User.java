import java.util.*;

public class User {
    private String username;
    private String password;
    private boolean online;

    public User(String username, String password, boolean online){
        this.username = username;
        this.password = password;
        this.online = online;
    }

    public String toString(){
        return this.username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    public boolean isOnline(){
        return this.online;
    }

    public void setOnline(boolean status){
                this.online = status;
    }

}
