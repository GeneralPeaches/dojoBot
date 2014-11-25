/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;



import java.util.ArrayList;
import java.util.HashMap;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.*;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author General Peaches and Slastic
 */
public class Commands extends ListenerAdapter{
    
    /**
     * So in addition to hard coded commands I'm thinking of adding
     * in two Map<String, String> for custom commands (mod only and 
     * regular users) that will be saved to a txt file or something 
     * when shutting down.
     * 
     * This is obviously a stretch goal kind of thing though.
     * 
     * Also a custom constructor that takes the channel name as a
     * parameter in order to load the appropriate custom commands
     * for that channel. (Assuming bot hosted on a server)
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

    /*
    public Commands(String channelName){
        BufferedReader reader = new BufferedReader(new FileReader(channelName + ".txt"));
        String line = null;
        
        while((line = reader.readLine()) != null){
            
        }
    }
    */
    
    HashMap<String, String> customComs = new HashMap<String, String>();
    HashMap<String, String> modComs = new HashMap<String, String>();
    
    //bot's behavior for messages
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
            case "!music":
                if(messageArray.length == 1)
                    message.respond("the playlist is ...");
                //response = playlist();
                break;
            case "!addcom":
                if (message.getChannel().getOps().contains(message.getUser())){
                    response = addCom(messageArray);
                    message.getChannel().send().message(response);
                }
                else{
                    message.respond("You are not allowed to add commands.");
                }
                break;
            case "!delcom":
                break;
            case "!editcom":
                break;
            default:
                if(message.getMessage().startsWith("!")){
                    customCommands(message);
                }
                break;
        }
    }

    //adds the command to the appropriate HashMap if properly formatted
    /**
     * 
     * @param messageArray
     * @return response based on the array's contents
     * 
     * Proper format for adding command:
     * !addcom [command] [permissions] [output]
     * [command] must start with an exclamation point
     * [permissions] is either 'm' or 'e'
     *   - 'm' is for mod only commands
     *   - 'e' is for everyone
     * [output] is the message you want the bot to put in chat
     */    
    private String addCom(String[] messageArray){
        String output = "";
        
        //check that there are enough arguments for the command
        if(messageArray.length < 4){
            return("Not enough arguments.");
        }
        
        //check that the second argument starts with '!'
        if(!messageArray[1].startsWith("!")){
            return("Commands must start with '!'");
        }
        
        //check that the third argument is either "-m" or "-e"
        if(!messageArray[2].equals("-m") && !messageArray[2].equals("-e")){
            return("Third argument must be '-m' or '-e'");
        }
        
        //build the command output from the message array
        output += messageArray[3];
        for (int i = 4; i < messageArray.length; i++){
            output += " " + messageArray[i];
        }
        
        if(messageArray[2].equals("-m")){
            modComs.put(messageArray[1], output);
        }
        else{
            customComs.put(messageArray[1], output);
        }
        
        return ("Command added successfully!");
    }
    
    //this method handles custom commands
    private void customCommands(MessageEvent message){
        String command = message.getMessage();
        
        //checks if the command is in custom mod commands
        if(modComs.containsKey(command)){
            if(message.getChannel().getOps().contains(message.getUser())){
                message.getChannel().send().message(modComs.get(command));
            }
            else{
                message.respond("You're not allowed to use this command.");
            }
        }
        //checks if the command is in the custom commands available to everyone
        else if(customComs.containsKey(command)){
            message.getChannel().send().message(customComs.get(command));
        }
        else{
            message.respond("No such command exists");
        }
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
}
