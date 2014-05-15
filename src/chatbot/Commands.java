/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import org.jibble.pircbot.User;
import org.jibble.pircbot.*;

import java.util.ArrayList;

/**
 *
 * @author JJ
 */
public class Commands {
    
    public String getResponse(String channel, String sender, String login, String hostname, String message, ArrayList<String> mods) {
        String response = "";
        
        switch(message){
            case "!time":
                response = time(sender);
                break;
            case "!permit":
                //need to check if the person who send the message
                //is a mod, then if so permit the user.
                String hmm =
                System.out.println(mods);
                if(mods.contains(sender)){
                    response = sender + " may permit user";
                }
                else
                response = sender + " may not permit user";
                //String user = "";
                //response = permitUser(user);
                break;
            case "!music":
                response = playlist();
                break;
            default:
                break;
        }
        
        if(message.equalsIgnoreCase("!time")){
            response = time(sender);
        }
        return response;
    }
    
    //post the time in the chat (basically a useless function
    private String time(String sender){
        String time = new java.util.Date().toString();
        String response = sender + ": The time is now " + time;
        return response;
    }
    
    private String playlist(){
        //TODO: Implement this command
        return "";
    }
    
    //the param for this should be the user to be permited
    //need to separate the username from the command and pass it in
    //easy to do, just need the time
    private String permitUser(String user){
        //TODO: Implement this commnand to allow sender to post links
        return "";
    }
}
