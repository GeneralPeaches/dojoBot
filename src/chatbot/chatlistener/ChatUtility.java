package chatbot.chatlistener;

import java.util.ArrayList;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import java.util.HashMap;
import java.util.Random;
import org.pircbotx.hooks.events.ConnectEvent;

import org.pircbotx.hooks.events.MessageEvent;

import chatbot.gui.GuiPublisher;
import chatbot.gui.GuiSubscriber;
/**
 * Created by Slastic on 5/27/2015.
 */
public class ChatUtility extends ListenerAdapter implements GuiSubscriber, GuiPublisher 
{
    private boolean _queueActive = true;
    private boolean _chatUtilActive = true;
    private boolean _pollOpen = false;
    private boolean _giveawayOpen = false;

    private LinkedList<String> _playerQueue = new LinkedList<>();
    private ArrayList<String> _giveawayEntries = new ArrayList<>();
    private HashMap<String,Integer> _poll = new HashMap<>();
    private ArrayList<GuiSubscriber> _subscribers = new ArrayList<>();

    private Random _rand = new Random();

    public ChatUtility(GuiSubscriber sub) 
    {
        register(sub);
    }
    
    @Override
    public void onConnect(ConnectEvent event) 
    {
        //event.getBot().sendCAP().request("twitch.tv/tags");
        event.getBot().sendCAP().request("twitch.tv/membership");
        event.getBot().sendCAP().request("twitch.tv/commands");
    }
    
    @Override
    public void onMessage(MessageEvent message) 
    {
        String newMessage = message.getMessage();
        String response;

        //split the message on spaces to identify the command
        String[] messageArray = newMessage.split(" ");

        switch (messageArray[0]) 
        { 
            case "!utility":
                if (message.getChannel().getOps().contains(message.getUser())) 
                {
                    if (messageArray.length == 2) 
                    {
                        if (messageArray[1].equals("off")) 
                        {
                            _chatUtilActive = false;
                        } 
                        else if (messageArray[1].equals("on")) 
                        {
                            _chatUtilActive = true;
                        }
                    }
                }
            //music commands
            case "!music":
                if (messageArray.length == 1)
                {
                    message.respond("the playlist is ...");
                }
                //response = playlist();
                break;
            // queue commands
            case "!queue":
                if(messageArray.length == 2) 
                {
                    if (message.getChannel().getOps().contains(message.getUser())) 
                    {
                        if (messageArray[1].equals("off"))
                        {
                            _queueActive = false;
                        } 
                        else if (messageArray[1].equals("on")) 
                        {
                            _queueActive = true;
                        }
                    }
                }
                break;
            case "!join":
                if (_queueActive) 
                {
                    if (messageArray.length == 2)
                    {
                        if (!_playerQueue.contains(messageArray[1])) 
                        {
                            _playerQueue.add(messageArray[1]);
                            message.respond(messageArray[1] + " has been added to the queue.");
                        }
                        else
                        {
                            message.respond(messageArray[1] + " is already in the queue");
                        }
                    }
                    else 
                    {
                        if (!_playerQueue.contains(message.getUser().getNick())) 
                        {
                            _playerQueue.add(message.getUser().getNick());
                            message.respond("You have been added to the queue.");
                        }
                        else
                        {
                            message.respond("You are already in the queue!");
                        }
                    }
                }
                break;
            case "!leave":
                if (_queueActive) 
                {
                    if (!_playerQueue.contains(message.getUser().getNick()))
                    {
                        message.respond("You aren't in the queue!");
                    }
                    else
                    {
                        _playerQueue.remove(message.getUser().getNick());
                        message.respond("You have been removed from the queue");
                    }
                }
                break;
            case "!remove":
                if (_queueActive) 
                {
                    if (messageArray.length == 2) 
                    {
                        if (!_playerQueue.contains(messageArray[1]))
                        {
                            message.respond(messageArray[1] + " is not in the queue!");
                        }
                        else 
                        {
                            _playerQueue.remove(messageArray[1]);
                            message.respond(messageArray[1] + " has been removed from the queue");
                        }
                    }
                    else 
                    {
                        message.respond("Please specify an item to remove from the queue.");
                    }
                }
            case "!get":
                if (_queueActive)
                {
                    if (message.getChannel().getOps().contains(message.getUser())) 
                    {
                        response = nextItem();
                        message.getChannel().send().message(response);
                    }
                    else 
                    {
                        message.respond("You are not allowed to get items from the queue.");
                    }
                }
                break;
            case "!getPlayers":
                if (_queueActive) 
                {
                    if (message.getChannel().getOps().contains(message.getUser())) 
                    {
                        if (messageArray.length < 2) 
                        {
                            message.respond("Please specify a number of players");
                        }
                        response = getPlayers(messageArray[1]);
                        message.getChannel().send().message(response);
                    }
                    else 
                    {
                        message.respond("You are not allowed to get players from the queue.");
                    }
                }
                break;
            //polling commands
            case "!createPoll":
                if (_chatUtilActive) 
                {
                    if (message.getChannel().getOps().contains(message.getUser()))
                    {
                        if (!_poll.isEmpty()) 
                        {
                            _poll.clear();
                        }
                        for (int i = 1; i < messageArray.length; i++) 
                        {
                            _poll.put(messageArray[i], 0);
                        }
                        
                        message.respond("poll created");
                        _pollOpen = true;
                    }
                }
                break;
            case "!vote":
                if (_pollOpen)
                {
                    if (_poll.containsKey(messageArray[1])) 
                    {
                        _poll.put(messageArray[1],_poll.get(messageArray[1])+1);
                        message.respond("vote accepted");
                    }
                    //incrememnt choice in the poll
                }
                break;
            case "closePoll":
                if (_chatUtilActive)
                {
                    if (message.getChannel().getOps().contains(message.getUser())) 
                    {
                        _pollOpen = false;
                        message.respond(_poll.toString());
                        //return the results of the poll
                    }
                }
                break;
            case "!createGiveaway":
                if (_chatUtilActive)
                {
                    if (message.getChannel().getOps().contains(message.getUser())) 
                    {
                        _giveawayOpen = true;
                        _giveawayEntries.clear();
                        message.respond("giveaway is now open type !raffle for a chance to win");
                    }
                }
                break;
            case "!closeGiveaway":
                if (_chatUtilActive) 
                {
                    if (message.getChannel().getOps().contains(message.getUser()))
                    {
                        _giveawayOpen = false;
                        message.respond("giveaway closed");
                    }
                }
                break;
            case "!raffle":
                if (_giveawayOpen) 
                {
                    if (!_giveawayEntries.contains(message.getUser().getNick())) 
                    {
                        _giveawayEntries.add(message.getUser().getNick());
                    }
                }
                break;
            case "!winner":
                if (message.getChannel().getOps().contains(message.getUser()))
                {
                    int number = _rand.nextInt(_giveawayEntries.size());

                    String winner = _giveawayEntries.get(number);
                    message.respond("and the winner is: " + winner);
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void notify(String message) 
    {
        switch (message)
        {
            case "queue":
                _queueActive = !_queueActive;
                break;
            case "utility":
                _chatUtilActive = !_chatUtilActive;
                break;
        }
    }
    
    //method to get next element from the queue
    private String nextItem()
    {
        if (!_playerQueue.isEmpty())
        {
            return _playerQueue.pollFirst();
        }
        else 
        {
            return "Queue is empty.";
        }
    }
    
    //method to get a number of players from the player queue
    private String getPlayers(String num)
    {
        int number = Integer.parseInt(num);
        String players = "";

        //if the queue isn't empty
        if (!_playerQueue.isEmpty()) 
        {
            //if there are fewer players in the queue than the number asked for
            //set number to size
            if (_playerQueue.size() < number) 
            {
                number = _playerQueue.size();
            }

            for (int i = 0; i < number; i++) 
            {
                String user = _playerQueue.pollFirst();
                players += user + ",";
            }
        }
        else 
        {
            players = "There are no players in the queue!";
        }

        return players;
    }

    private String playlist() 
    {
        //TODO: Implement this command
        return "";
    }
    
    @Override
    public void register(GuiSubscriber sub) 
    {
        _subscribers.add(sub);
    }
    
    @Override
    public void remove(GuiSubscriber sub) 
    {
        _subscribers.remove(sub);
    }
    
    @Override
    public void broadcast(String message)
    {
        for(GuiSubscriber sub:_subscribers) 
        {
            sub.notify(message);
        }
    }
}
