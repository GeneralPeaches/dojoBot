package chatbot;

import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.User;

import java.util.ArrayList;

public class SpamControl extends ListenerAdapter{
    private ArrayList<String> bannablePhrases = new ArrayList<String>();
    private ArrayList<String> permittedUsers = new ArrayList<String>();
    
    public SpamControl(){
        bannablePhrases = populatePhrases();
    }

    public void onMessage(MessageEvent message){
        String currentMessage = message.getMessage();
        //there has to be a cleaner way to do this
        
        String[] messageArray = message.getMessage().split(" ");
        
        if(messageArray[0].equals("!permit")){
            if(messageArray.length == 2) {
                    if (message.getUser().isIrcop()){
                        //message.respond(permitUser(messageArray[1]));
                    }
            }
        }
        
        if(!message.getUser().isIrcop()){
            for(String test : bannablePhrases){
                if(currentMessage.contains(test)){
                    purge(message);
                    break;
                }
            }
            //if(currentMessage.contains(".com")) {
               // purge(message);
            //}
        }
    }

    public void purge(MessageEvent spamMessage){
        spamMessage.respond("come on now that's not allowed here");

        spamMessage.getChannel().send().message("/timeout "+spamMessage.getUser().getNick()+ " 1");
    }

    public ArrayList <String> populatePhrases(){
        //method primarily designed for future implementation of reading in a file filled with all things to be timed out, including ascii art, just currently a placeholder for proof of concept
        ArrayList theList = new ArrayList();
        theList.add(".com");
        theList.add(",net");
        theList.add(".net");
        theList.add(",com");
        theList.add("goo.gl");
        theList.add("bit.ly");
        theList.add("owl.ly");
        return theList;


    }
    
    private String permitUser(String user){
        //TODO: Implement this commnand to allow sender to post links
        //functionality of adding user to data set that is allowed to post links, will need to also be used in the auto chat timeouts implementation
        permittedUsers.add(user);
        String response = (user + " may post a link");
        return response;
    }
}