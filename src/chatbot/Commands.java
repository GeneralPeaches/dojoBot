/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.util.ArrayList;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author General Peaches and Slastic
 */
public class Commands extends ListenerAdapter implements GuiSubscriber {

    private final DatabaseManagement manager = new DatabaseManagement();
    private LinkedList<String> playerQueue = new LinkedList<>();
    
    private boolean queueActive = true;
    private boolean commandsActive = true;

    //bot's behavior for messages
    @Override
    public void onMessage(MessageEvent message) {
        String newMessage = message.getMessage();
        String response;

        //split the message on spaces to identify the command
        String[] messageArray = newMessage.split(" ");

        switch (messageArray[0]) {
            case "!music":
                if (messageArray.length == 1)
                    message.respond("the playlist is ...");
                //response = playlist();
                break;
            //command to make a custom command for the bot
            case "!addcom":
                if(commandsActive){
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = addCom(messageArray, message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } else {
                        message.respond("You are not allowed to add commands.");
                    }
                }
                break;
            case "!commands":
                if(commandsActive){
                    if(messageArray.length ==1){
                        ArrayList<String> commands = manager.getCommands(message.getChannel().getName());
                        String commandList = "The custom commands available to everyone for this channel are: ";
                        while(!commands.isEmpty()){
                            commandList += commands.remove(0) + ", ";
                        }
                        message.getChannel().send().message(commandList);
                    }
                }
                break;
            //command to delete a custom command from the bot
            case "!delcom":
                if(commandsActive){
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = delCom(messageArray[1], message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } else {
                        message.respond("You are not allowed to remove commands.");
                    }
                }
                break;
            //command to edit a custom command the bot has
            case "!editcom":
                if(commandsActive){
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = editCom(messageArray, message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } else {
                        message.respond("You are not allowed to edit commands.");
                    }
                }
                break;
            case "!join":
                if(queueActive){
                    if(!playerQueue.contains(message.getUser().getNick())){
                        playerQueue.add(message.getUser().getNick());
                        message.respond("You have been added to the queue.");
                    }
                    else{
                        message.respond("You are already in the queue!");
                    }
                }
                break;
            case "!leave":
                if(queueActive){
                    if(!playerQueue.contains(message.getUser().getNick())){
                        message.respond("You aren't in the queue!");
                    }
                    else{
                        playerQueue.remove(message.getUser().getNick());
                        message.respond("You have been removed from the queue");
                    }
                }
                break;
            case "!getPlayers":
                if(queueActive){
                    if(messageArray.length < 2){
                        message.respond("Please specify a number of players");
                    }
                    else{
                        if(message.getChannel().getOps().contains(message.getUser())){
                            response = getPlayers(messageArray[1]);
                            message.getChannel().send().message(response);
                        }
                        else {
                            message.respond("You are not allowed to get players from the queue.");
                        }
                    }
                }
                break;
            //default message handling for custom commands
            default:
                if(commandsActive){
                    if (message.getMessage().startsWith("!") && !messageArray[0].equals("!permit")&& !messageArray[0].equals("!spam")) {
                        customCommands(message);
                    }
                }
                break;
        }
    }

    //method to get a number of players from the player queue
    private String getPlayers(String num){
        int number = Integer.parseInt(num);
        String players = "";
        
        //if the queue isn't empty
        if(!playerQueue.isEmpty()){
            //if there are fewer players in the queue than the number asked for
            //set number to size
            if(playerQueue.size() < number){
                number = playerQueue.size();
            }

            for(int i = 0; i < number; i++){
                String user = playerQueue.pollFirst();
                players += user + ",";
                playerQueue.add(user);            
            }
        }
        else {
            players = "There are no players in the queue!";
        }
        
        return players;
    }

    /**
     * @param messageArray
     * @return response based on the array's contents
     * <p/>
     * Proper format for adding command:
     * !addcom [command] [permissions] [output]
     * [command] must start with an exclamation point
     * [permissions] is either 'm' or 'e'
     * - 'm' is for mod only commands
     * - 'e' is for everyone
     * [output] is the message you want the bot to put in chat
     */
    private String addCom(String[] messageArray, String channel) {
        String output = "";

        //check that the command isn't one of the precoded ones
        if (messageArray[2].equals("!addcom") || messageArray[2].equals("!editcom")
                || messageArray[2].equals("!delcom") || messageArray[2].equals("!permit") || messageArray[2].equals("!music") || messageArray[2].equals("!join") 
                || messageArray[2].equals("!leave") || messageArray[2].equals("!getPlayers")) {
            return ("Cannot make commands with the same name as default commands");
        }

        //check that there are enough arguments for the command
        if (messageArray.length < 4) {
            return ("Not enough arguments");
        }

        //check that the second argument starts with '!'
        if (!messageArray[1].startsWith("!")) {
            return ("Commands must start with '!'");
        }

        //check that the third argument is either "-m" or "-e"
        if (!messageArray[2].equals("-m") && !messageArray[2].equals("-e")) {
            return ("Third argument must be '-m' or '-e'");
        }

        //build the command output from the message array
        output += messageArray[3];
        for (int i = 4; i < messageArray.length; i++) {
            output += " " + messageArray[i];
        }
        int id = manager.getDatabaseSize()+1;
        // build sql statement
        String statement = "INSERT INTO customcommands ('id', 'command', 'response', 'permission', 'channel') VALUES( '" +id;
        statement +="', '"+ messageArray[1];
        statement += "', '" + output;
        statement += "', '" + messageArray[2];
        statement += "', '" + channel + "')";
        manager.connectToDatabase(statement);

        return ("Custom command added successfully!");
    }

    /**
     * Delete a custom command from the bot
     *
     * @param
     * @return message regarding success of command removal
     */
    private String delCom(String command, String channel) {
        String statement = "DELETE FROM customCommands WHERE command='" + command + "' AND channel = '"+channel+"'; ";
        manager.connectToDatabase(statement);
        return ("Custom command " + command + " was successfully removed.");
    }

    /**
     * Edit an existing custom command
     *
     * @param messageArray 
     * supports the following !editcom formats
     *      !editcom [command] -m/-e
     *          -changes only the permissions of the command
     *      !editcom [command] [output]
     *          -changes only the output of the command
     *      !editcom [command] -m/-e [output]
     *          -changes both the permissions and the output of the command
     */
    private String editCom(String[] messageArray, String channel) {
        // UPDATE commands SET response= 'newtext' where command = command AND channel= channel;
        String command = messageArray[1];
        String output = "";
        String[] info;

        //if there's no change to permissions then replace old output
        if (!messageArray[2].equals("-e") && !messageArray[2].equals("-m")) {
            //build output string
            output += messageArray[2];
            for (int i = 3; i < messageArray.length; i++) {
                output += " " + messageArray[i];
            }

            String statement = "UPDATE customcommands SET response= '" + output + "' WHERE command ='" + command + "' AND channel='" + channel + "';";
            manager.connectToDatabase(statement);
            return ("Command " + command + " successfully updated.");
        } 
        else {
            //if the command is just to change permissions
            if (messageArray.length < 4) {
                info = manager.getFromDatabase(command,channel);
                //if they want to make it mod only
                if (messageArray[2].equals("-m")) {
                    //if the command is already mod only
                    if (info[1].matches("-m")) {
                        return ("Command " + command + " is already mod only.");
                    } else {
                        String statement = "UPDATE customcommands SET permission= '" + messageArray[2] + "' WHERE command ='" + command + "' AND channel='" + channel + "';";
                        manager.connectToDatabase(statement);
                        //changes the permissions of the command
                        return ("Command " + command + " is now mod only.");
                    }
                }
                //if they want to make it usable for everyone
                else {
                    //if the command is already available to everyone
                    if (info[1].matches("-e")) {
                        return ("Command " + command + " is already available to everyone.");
                    } else {
                        //changes the permissions of the command
                        String statement = "UPDATE customcommands SET permission= '" + messageArray[2] + "' WHERE command ='" + command + "' AND channel='" + channel + "';";
                        manager.connectToDatabase(statement);

                        return ("Command " + command + " is available to everyone.");
                    }
                }
            } 
            else {
                //build output string
                output += messageArray[3];
                for (int i = 4; i < messageArray.length; i++) {
                    output += " " + messageArray[i];
                }
                String statement = "UPDATE customcommands SET response = ' " + output+  "' permission= '" + messageArray[2] + "' WHERE command ='" + command + "' AND channel='" + channel + "';";
                manager.connectToDatabase(statement);

                return ("Command " + command + " successfully updated.");
            }
        }
    }

    /**
     * This method processes custom commands
     *
     * @param message
     */
    private void customCommands(MessageEvent message) {
        String command = message.getMessage();
        String[]info;
        info = manager.getFromDatabase(command,message.getChannel().getName());

        //checks if the command is in custom mod commands
        if (info[1].matches("-m")) {
            if (message.getChannel().getOps().contains(message.getUser())) {
                message.getChannel().send().message(info[0]);
            } else {
                message.respond("You're not allowed to use this command.");
            }
        }
        //checks if the command is in the custom commands available to everyone
        else if (info[1].matches("-e")) {
            message.getChannel().send().message(info[0]);
        } else {
            message.respond("No such command exists");
        }
    }

    /**
     * This method may not exist at the end since I'm not sure what to do
     * about music
     *
     * @return
     */
    private String playlist() {
        //TODO: Implement this command
        return "";
    }
    
    @Override
    public void notify(String message) {
        switch (message){
            case "queue":
                queueActive = !queueActive;
                break;
            case "commands":
                commandsActive = !commandsActive;
                break;
        }
    }
}
