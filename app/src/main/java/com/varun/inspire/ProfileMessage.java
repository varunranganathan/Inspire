package com.varun.inspire;

/**
 * Created by vvvro on 1/2/2017.
 */

public class ProfileMessage {
    String username;
    String userEmail;
    String message;
    int pid, cid;
    public ProfileMessage(String tUsername, String tMessage){
        username = tUsername;
        message = tMessage;
    }
    public ProfileMessage(String tUsername, String tUserEmail, String tMessage, int tpid, int tcid){
        username = tUsername;
        userEmail = tUserEmail;
        message = tMessage;
        pid = tpid;
        cid =tcid;
    }
    public String getUsername(){
        return username;
    }
    public String getMessage(){
        return message;
    }
}
