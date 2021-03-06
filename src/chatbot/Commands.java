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
public class Commands extends ListenerAdapter implements GuiSubscriber, GuiPublisher {

    private final DatabaseManagement manager = new DatabaseManagement();
    private boolean commandsActive = true;
    
    private LinkedList<GuiSubscriber> subscribers = new LinkedList<>();
    private ArrayList<String> reservedCommands = new ArrayList<>();
    
    public Commands(GuiSubscriber sub) {
        register(sub);
        init();
    }
    
    //bot's behavior for messages
    @Override
    public void onMessage(MessageEvent message) {
        String newMessage = message.getMessage();
        String response;

        //split the message on spaces to identify the command
        String[] messageArray = newMessage.split(" ");
        
        switch (messageArray[0]) {
            case "!command":
                if (message.getChannel().getOps().contains(message.getUser())) {
                    if (messageArray.length == 2) {
                        if (messageArray[1].equals("off")) {
                            commandsActive = false;
                        } 
                        if (messageArray[1].equals("on")) {
                            commandsActive = true;
                        }
                    }
                }
            break;
            //command to make a custom command for the bot
            case "!addcom":
                if (commandsActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = addCom(messageArray, message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } 
                    else {
                        message.respond("You are not allowed to add commands.");
                    }
                }
                break;
            case "!commands":
                if (commandsActive) {
                    if (messageArray.length ==1) {
                        ArrayList<String> commands = manager.getCommands(message.getChannel().getName());
                        String commandList = "The custom commands available to everyone for this channel are: ";
                        while (!commands.isEmpty()) {
                            commandList += commands.remove(0) + ", ";
                        }
                        message.getChannel().send().message(commandList);
                    }
                }
                break;
            //command to delete a custom command from the bot
            case "!delcom":
                if (commandsActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = delCom(messageArray[1], message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } 
                    else {
                        message.respond("You are not allowed to remove commands.");
                    }
                }
                break;
            //command to edit a custom command the bot has
            case "!editcom":
                if (commandsActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = editCom(messageArray, message.getChannel().getName());
                        message.getChannel().send().message(response);
                    } 
                    else {
                        message.respond("You are not allowed to edit commands.");
                    }
                }
                break;

            //default message handling for custom commands
            default:
                if (commandsActive) {
                    if (message.getMessage().startsWith("!") && !messageArray[0].equals("!permit")&& !messageArray[0].equals("!spam")) {
                        customCommands(message);
                    }
                }
                break;
        }
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
        if (reservedCommands.contains(messageArray[1])) {
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
                info = manager.getCommandFromDatabase(command, channel);
                //if they want to make it mod only
                if (messageArray[2].equals("-m")) {
                    //if the command is already mod only
                    if (info[1].matches("-m")) {
                        return ("Command " + command + " is already mod only.");
                    } 
                    else {
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
                    } 
                    else {
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
        
        if (!reservedCommands.contains(command)) {
            String[]info;
            info = manager.getCommandFromDatabase(command, message.getChannel().getName());

            //checks if the command is in custom mod commands
            if (info[1].matches("-m")) {
                if (message.getChannel().getOps().contains(message.getUser())) {
                    message.getChannel().send().message(info[0]);
                } 
                else {
                    message.respond("You're not allowed to use this command.");
                }
            }
            //checks if the command is in the custom commands available to everyone
            else if (info[1].matches("-e")) {
                message.getChannel().send().message(info[0]);
            } 
            else {
                message.respond("No such command exists");
            }
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("commands")) {
                commandsActive = !commandsActive;
        }
    }
    
    @Override
    public void register(GuiSubscriber sub) {
        subscribers.add(sub);
    }
    
    @Override
    public void remove(GuiSubscriber sub) {
        subscribers.remove(sub);
    }
    
    @Override
    public void broadcast(String message) {
        for(GuiSubscriber sub:subscribers) {
            sub.notify(message);
        }
    }
    
    //horribly inelegant population of reserved command arraylist
    public void init() {
        reservedCommands.add("!addCom");
        reservedCommands.add("!editCom");
        reservedCommands.add("!delCom");
        reservedCommands.add("!commands");
        reservedCommands.add("!command");
        reservedCommands.add("!utility");
        reservedCommands.add("!music");
        reservedCommands.add("!queue");
        reservedCommands.add("!join");
        reservedCommands.add("!leave");
        reservedCommands.add("!getPlayers");
        reservedCommands.add("!createPoll");
        reservedCommands.add("!vote");
        reservedCommands.add("!closePoll");
        reservedCommands.add("!closeGiveaway");
        reservedCommands.add("!raffle");
        reservedCommands.add("!winner");
        reservedCommands.add("!permit");
        reservedCommands.add("!spam");
        reservedCommands.add("!addQuote");
        reservedCommands.add("!quoting");
        reservedCommands.add("!removeQuote");
        reservedCommands.add("!displayQuote");
        reservedCommands.add("!quotes");
    }
}
