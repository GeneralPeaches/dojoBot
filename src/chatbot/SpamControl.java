package chatbot;

import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.User;

import java.util.ArrayList;

public class SpamControl extends ListenerAdapter{
    private ArrayList<String> bannablePhrases = new ArrayList<String>();

    public SpamControl(){
        bannablePhrases = populatePhrases();
    }

    public void onMessage(MessageEvent message){
        String currentMessage = message.getMessage();
//there has to be a cleaner way to do this
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
}