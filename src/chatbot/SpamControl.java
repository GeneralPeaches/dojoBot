package chatbot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class SpamControl extends ListenerAdapter{
    private ArrayList<String> bannablePhrases = new ArrayList<>();
    private HashMap<String,Date> permittedUsers = new HashMap<>();
    
    public SpamControl(){
        bannablePhrases = populatePhrases();
    }

    @Override
    public void onMessage(MessageEvent message){
        String currentMessage = message.getMessage();
        
        String[] messageArray = message.getMessage().split(" ");
        
        if(messageArray[0].equals("!permit")){
            if(messageArray.length == 2) {
                if (message.getChannel().getOps().contains(message.getUser())){
                    message.respond(permitUser(messageArray[1]).toLowerCase());
                }
            }
        }
        
        if(!message.getChannel().getOps().contains(message.getUser())){
            for(String test : bannablePhrases){
                if(currentMessage.contains(test)){
                    System.out.println(message.getUser().getNick());
                    if(!permittedUsers.containsKey(message.getUser().getNick()) || permittedUsers.get(message.getUser().getNick()).before(new Date(System.currentTimeMillis()))){
                        purge(message);
                        break;
                    }
                }
            }
        }
    }

    private void purge(MessageEvent spamMessage){
        spamMessage.respond("come on now that's not allowed here");

        spamMessage.getChannel().send().message("/timeout "+spamMessage.getUser().getNick()+ " 1");
    }

    private ArrayList <String> populatePhrases(){
        //method primarily designed for future implementation of reading in a file filled with all things to be timed out, including ascii art, just currently a placeholder for proof of concept
        ArrayList theList = new ArrayList();
        theList.add(".com");
        theList.add(",net");
        theList.add(".net");
        theList.add(",com");
        theList.add("goo.gl");
        theList.add("bit.ly");
        theList.add("owl.ly");
        theList.add(".tv");
        theList.add(".org");
        
        /*
        bannablePhrases.add(".com");
        bannablePhrases.add(",net");
        bannablePhrases.add(".net");
        bannablePhrases.add(",com");
        bannablePhrases.add("goo.gl");
        bannablePhrases.add("bit.ly");
        bannablePhrases.add("owl.ly");
        bannablePhrases.add(".tv");
        bannablePhrases.add(".org");
        */
        
        return theList;
    }
    
    private String permitUser(String user){
        //TODO: Implement this commnand to allow sender to post links
        //functionality of adding user to data set that is allowed to post links, will need to also be used in the auto chat timeouts implementation
        
        Date endTime = new Date(System.currentTimeMillis() + 180000); 
        permittedUsers.put(user, endTime);
        
        String response = (user + " may post a link");
        return response;
    }
}