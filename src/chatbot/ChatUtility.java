package chatbot;

import java.util.ArrayList;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import java.util.HashMap;
import java.util.Random;
import org.pircbotx.hooks.events.ConnectEvent;

import org.pircbotx.hooks.events.MessageEvent;
/**
 * Created by Slastic on 5/27/2015.
 */
public class ChatUtility extends ListenerAdapter implements GuiSubscriber, GuiPublisher {
    private boolean queueActive = true;
    private boolean chatUtilActive = true;
    private boolean pollOpen = false;
    private boolean giveawayOpen = false;

    private LinkedList<String> playerQueue = new LinkedList<>();
    private ArrayList<String> giveawayEntries = new ArrayList<>();
    private HashMap<String,Integer> poll = new HashMap<>();
    private LinkedList<GuiSubscriber> subscribers = new LinkedList<>();

    Random rnd = new Random();

    public ChatUtility(GuiSubscriber sub) {
        register(sub);
    }
    
    @Override
    public void onConnect(ConnectEvent event) {
        //event.getBot().sendCAP().request("twitch.tv/tags");
        event.getBot().sendCAP().request("twitch.tv/membership");
        event.getBot().sendCAP().request("twitch.tv/commands");
    }
    
    @Override
    public void onMessage(MessageEvent message) {
        String newMessage = message.getMessage();
        String response;

        //split the message on spaces to identify the command
        String[] messageArray = newMessage.split(" ");

        switch (messageArray[0]) { 
            case "!utility":
                if (message.getChannel().getOps().contains(message.getUser())) {
                    if (messageArray.length == 2) {
                        if (messageArray[1].equals("off")) {
                            chatUtilActive = false;
                        } 
                        if (messageArray[1].equals("on")) {
                            chatUtilActive = true;
                        }
                    }
                }
            //music commands
            case "!music":
                if (messageArray.length == 1)
                    message.respond("the playlist is ...");
                //response = playlist();
                break;
            // queue commands
            case "!queue":
                if(messageArray.length == 2) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        if (messageArray[1].equals("off")) {
                            queueActive = false;
                        } 
                        if (messageArray[1].equals("on")) {
                            queueActive = true;
                        }
                    }
                }
                break;
            case "!join":
                if (queueActive) {
                    if (messageArray.length == 2) {
                        if (!playerQueue.contains(messageArray[1])) {
                            playerQueue.add(messageArray[1]);
                            message.respond(messageArray[1] + " has been added to the queue.");
                        }
                        else{
                            message.respond(messageArray[1] + " is already in the queue");
                        }
                    }
                    else {
                        if (!playerQueue.contains(message.getUser().getNick())) {
                            playerQueue.add(message.getUser().getNick());
                            message.respond("You have been added to the queue.");
                        }
                        else{
                            message.respond("You are already in the queue!");
                        }
                    }
                }
                break;
            case "!leave":
                if (queueActive) {
                    if(!playerQueue.contains(message.getUser().getNick())){
                        message.respond("You aren't in the queue!");
                    }
                    else{
                        playerQueue.remove(message.getUser().getNick());
                        message.respond("You have been removed from the queue");
                    }
                }
                break;
            case "!remove":
                if (queueActive) {
                    if (messageArray.length == 2) {
                        if (!playerQueue.contains(messageArray[1])){
                            message.respond(messageArray[1] + " is not in the queue!");
                        }
                        else {
                            playerQueue.remove(messageArray[1]);
                            message.respond(messageArray[1] + " has been removed from the queue");
                        }
                    }
                    else {
                        message.respond("Please specify an item to remove from the queue.");
                    }
                }
            case "!get":
                if (queueActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        response = nextItem();
                        message.getChannel().send().message(response);
                    }
                    else {
                        message.respond("You are not allowed to get items from the queue.");
                    }
                }
                break;
            case "!getPlayers":
                if (queueActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        if (messageArray.length < 2) {
                            message.respond("Please specify a number of players");
                        }
                        response = getPlayers(messageArray[1]);
                        message.getChannel().send().message(response);
                    }
                    else {
                        message.respond("You are not allowed to get players from the queue.");
                    }
                }
                break;
            //polling commands
            case "!createPoll":
                if (chatUtilActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        if (!poll.isEmpty()) {
                            poll.clear();
                        }
                        for (int i = 1; i < messageArray.length; i++) {
                            poll.put(messageArray[i], 0);
                        }
                    message.respond("poll created");

                    pollOpen = true;
                    }
                }
                break;
            case "!vote":
                if (pollOpen) {
                    if (poll.containsKey(messageArray[1])) {
                        poll.replace(messageArray[1],poll.get(messageArray[1]),poll.get(messageArray[1])+1);
                        message.respond("vote accepted");
                    }
                    //incrememnt choice in the poll
                }
                break;
            case "closePoll":
                if (chatUtilActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        pollOpen = false;
                        message.respond(poll.toString());
                        //return the results of the poll
                    }
                }
                break;
            case "!createGiveaway":
                if (chatUtilActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        giveawayOpen = true;
                        giveawayEntries.clear();
                        message.respond("giveaway is now open type !raffle for a chance to win");
                    }
                }
                break;
            case "!closeGiveaway":
                if (chatUtilActive) {
                    if (message.getChannel().getOps().contains(message.getUser())) {
                        giveawayOpen = false;
                        message.respond("giveaway closed");
                    }
                }
                break;
            case "!raffle":
                if (giveawayOpen) {
                    if (!giveawayEntries.contains(message.getUser().getNick())) {
                        giveawayEntries.add(message.getUser().getNick());
                    }
                }
                break;
            case "!winner":
                if (message.getChannel().getOps().contains(message.getUser())) {
                    int number = rnd.nextInt(giveawayEntries.size());

                    String winner = giveawayEntries.get(number);
                    message.respond("and the winner is: " + winner);
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void notify(String message) {
        switch (message){
            case "queue":
                queueActive = !queueActive;
                break;
            case "utility":
                chatUtilActive = !chatUtilActive;
                break;
        }
    }
    
    //method to get next element from the queue
    private String nextItem() {
        if (!playerQueue.isEmpty()) {
            return playerQueue.pollFirst();
        }
        else {
            return "Queue is empty.";
        }
    }
    
    //method to get a number of players from the player queue
    private String getPlayers(String num){
        int number = Integer.parseInt(num);
        String players = "";

        //if the queue isn't empty
        if (!playerQueue.isEmpty()) {
            //if there are fewer players in the queue than the number asked for
            //set number to size
            if (playerQueue.size() < number) {
                number = playerQueue.size();
            }

            for (int i = 0; i < number; i++) {
                String user = playerQueue.pollFirst();
                players += user + ",";
            }
        }
        else {
            players = "There are no players in the queue!";
        }

        return players;
    }

    private String playlist() {
        //TODO: Implement this command
        return "";
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
}
