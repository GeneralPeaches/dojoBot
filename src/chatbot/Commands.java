/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;



import java.util.ArrayList;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.*;
import org.pircbotx.hooks.events.MessageEvent;



/**
 *
 * @author JJ
 */
public class Commands extends ListenerAdapter{
    
    /**
     * So in addition to hard coded commands I'm thinking of adding
     * in a Map<String, String> for custom commands that will be
     * saved to a txt file or something when shutting down.
     * This is obviously a stretch goal kind of thing though.
     * 
     * Also a custom constructor that takes the channel name as a
     * parameter in order to load the appropriate custom commands
     * for that channel.
     */

    /**
     * We'll be using these two lines for all commands.
     * The first one is for general messages
     * The second is for things we want in Dojobot's text color
     * 
     * Tell user: event.getChannel().send().message("");
     * 
     * Tell channel: event.getChannel().send().action("");
     */
    
    //bot's behavior for messages

    ArrayList<String> permittedUsers = new ArrayList<String>();
    public void onMessage(MessageEvent message){
        String newMessage = message.getMessage();
        String response = "";

       // message.getChannel().send().message("Test message");
        
        String[] messageArray = newMessage.split(" ");


        switch(messageArray[0]){
            case "!time":
                if(messageArray.length == 1)
                    response = time(message.getUser().getNick());
                message.respond(response);
                break;
            case "!permit":
                //need to check if the person who send the message
                //is a mod, then if so permit the user.

                if(messageArray.length == 2) {
                    if (message.getUser().isIrcop()){
                        message.respond(permitUser(messageArray[1]));
                    }
                    }

                break;
            case "!music":
                if(messageArray.length == 1)
                    message.respond("the playlist is ...");
                //response = playlist();
                break;
            default:
                break;
        }
    }
    
    //getResponse implementation for MessageEvent objects
    public String getResponse(MessageEvent event){
        User user = event.getUser();
        String message = event.getMessage();
        Channel channel = event.getChannel();
        
        
        switch(message){
            case "!permit":
                break;
            default:
                break;
        }
        
        return "";
    }

    //post the time in the chat (basically a useless function
    private String time(String sender){
        String time = new java.util.Date().toString();
        String response = "The time is now " + time;
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
        //functionality of adding user to data set that is allowed to post links, will need to also be used in the auto chat timeouts implementation
       permittedUsers.add(user);
        String response = (user + " may post a link");
        return response;
    }
}
