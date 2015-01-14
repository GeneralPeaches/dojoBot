/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;



import java.util.HashMap;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author General Peaches and Slastic
 */
public class Commands extends ListenerAdapter{
    
    /**
     * Custom commands now possible, just need to save them to an external file
     * (or two)
     * 
     * Also a custom constructor that takes the channel name as a
     * parameter in order to load the appropriate custom commands
     * for that channel. (Assuming bot hosted on a server)
     * 
     * @param channelName
     */

    //Constructor for loading custom commands from a saved file
    public Commands(String channelName){
        
        
    }
    
    
    HashMap<String, String> customComs = new HashMap<>();
    HashMap<String, String> modComs = new HashMap<>();
    
    //bot's behavior for messages
    @Override
    public void onMessage(MessageEvent message){
        String newMessage = message.getMessage();
        String response;
        
        //split the message on spaces to identify the command
        String[] messageArray = newMessage.split(" ");

        switch(messageArray[0]){
            case "!music":
                if(messageArray.length == 1)
                    message.respond("the playlist is ...");
                //response = playlist();
                break;
            //command to make a custom command for the bot
            case "!addcom":
                if (message.getChannel().getOps().contains(message.getUser())){
                    response = addCom(messageArray);
                    message.getChannel().send().message(response);
                }
                else{
                    message.respond("You are not allowed to add commands.");
                }
                break;
            //command to delete a custom command from the bot
            case "!delcom":
                if (message.getChannel().getOps().contains(message.getUser())){
                    response = delCom(messageArray[1]);
                    message.getChannel().send().message(response);
                }
                else{
                    message.respond("You are not allowed to remove commands.");
                }
                break;
            //command to edit a custom command the bot has
            case "!editcom":
                if (message.getChannel().getOps().contains(message.getUser())){
                    response = editCom(messageArray);
                    message.getChannel().send().message(response);
                }
                else{
                    message.respond("You are not allowed to edit commands.");
                }
                break;
            //default message handling for custom commands
            default:
                if(message.getMessage().startsWith("!") && !messageArray[0].equals("!permit")){
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
        
        //check that the command isn't one of the precoded ones
        if(messageArray[2].equals("!addcom") || messageArray[2].equals("!editcom") 
                || messageArray[2].equals("!delcom") || messageArray[2].equals("!permit") || messageArray[2].equals("!music")){
            return ("Cannot make commands with the same name as default commands");
        }
        
        //check that there are enough arguments for the command
        if(messageArray.length < 4){
            return("Not enough arguments");
        }
        
        //check that the second argument starts with '!'
        if(!messageArray[1].startsWith("!")){
            return("Commands must start with '!'");
        }
        
        //check that the third argument is either "-m" or "-e"
        if(!messageArray[2].equals("-m") && !messageArray[2].equals("-e")){
            return("Third argument must be '-m' or '-e'");
        }
        
        if(modComs.containsKey(messageArray[1]) || customComs.containsKey(messageArray[1])){
            return ("Command already exists");
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
        
        return ("Custom command added successfully!");
    }
    
    /**
     * Delete a custom command from the bot
     * @param message
     * @return message regarding success of command removal
     */
    private String delCom(String command){
        
        //check if the command does exist
        if(!modComs.containsKey(command) && !customComs.containsKey(command)){
            return ("Custom command " + command + " does not exist");
        }
        //check if it's a mod command
        else if(modComs.containsKey(command)){
            modComs.remove(command);
        }
        else{
            customComs.remove(command);
        }
        
        return ("Custom command " + command + " was successfully removed.");
    }
    
    /**
     * Edit an existing custom command
     * @param messageArray 
     * 
     * supports the following !editcom formats
     * 
     * !editcom [command] -m/-e
     *  -changes only the permissions of the command
     * 
     * !editcom [command] [output]
     *  -changes only the output of the command
     * 
     * !editcom [command] -m/-e [output]
     *  -changes both the permissions and the output of the command
     */
    private String editCom(String[] messageArray){
        
        String command = messageArray[1];
        String output = "";
        
        //if the command does not exist it can't be edited
        if(!modComs.containsKey(command) && !customComs.containsKey(command)){
            return ("Command " + command + " does not exist");
        }
        
        //if there's no change to permissions then replace old output
        if(!messageArray[2].equals("-e") || !messageArray[2].equals("-m")){
            //build output string
            output += messageArray[2];
            for (int i = 3; i < messageArray.length; i++){
                output += " " + messageArray[i];
            }
            
            //replaces the old command with the new one
            if(modComs.containsKey(command)){
                modComs.put(command, output);
            }
            else if(customComs.containsKey(command)){
                customComs.put(command, output);
            }
            
            return ("Command " + command + " successfully updated.");
        }
        else{
            //if the command is just to change permissions
            if(messageArray.length < 4){
                //if they want to make it mod only
                if(messageArray[2].equals("-m")){
                    //if the command is already mod only
                    if(modComs.containsKey(command)){
                        return ("Command " + command + " is already mod only.");
                    }
                    else{
                        //changes the permissions of the command
                        output = customComs.get(command);
                        customComs.remove(command);
                        modComs.put(command, output);
                        
                        return ("Command " + command + " is now mod only.");
                    }
                }
                //if they want to make it usable for everyone
                else{
                    //if the command is already available to everyone
                    if(customComs.containsKey(command)){
                        return ("Command " + command + " is already available to everyone.");
                    }
                    else{
                        //changes the permissions of the command
                        output = modComs.get(command);
                        modComs.remove(command);
                        customComs.put(command, output);
                        
                        return ("Command " + command + " is available to everyone.");
                    }
                }
            }
            else{
                //build output string
                output += messageArray[3];
                for (int i = 4; i < messageArray.length; i++){
                    output += " " + messageArray[i];
                }
                
                if(customComs.containsKey(command)){
                    if(messageArray[2].equals("-e")){
                        customComs.put(command, output);
                    }
                    else{
                        customComs.remove(command);
                        modComs.put(command, output);
                    }
                    return ("Command " + command + " successfully updated.");
                }
                else{
                    if(messageArray[2].equals("-m")){
                        modComs.put(command, output);
                    }
                    else{
                        modComs.remove(command);
                        customComs.put(command, output);
                    }
                    return ("Command " + command + " successfully updated.");
                }
            }
        }
    }
    
    /**
     * This method processes custom commands
     * @param message 
     */
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

    /**
     * This method may not exist at the end since I'm not sure what to do
     * about music
     * @return 
     */
    private String playlist(){
        //TODO: Implement this command
        return "";
    }
}
