package chatbot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Random;

/**
 * Created by Slastic on 6/9/2015.
 */
public class Quote extends ListenerAdapter implements GuiSubscriber {

    private boolean quotesActive = true;

    private final DatabaseManagement manager = new DatabaseManagement();

    public void onMessage(MessageEvent message){
        String[] messageArray = message.getMessage().split(" ");
        String response;
        String [] quote;

        switch(messageArray[0]){
            case "!addQuote":
                response = addQuote(messageArray, message.getChannel().getName());
                message.getChannel().send().message(response);
                break;
            case "!quoting":
                message.respond("to add a quote to the database simply type !addQuote (the person who you are quotings name) (the quote)");
                break;
            case "!removeQuote":
                int index = Integer.parseInt(messageArray[1]);
                response = removeQuote(index, message.getChannel().getName());
                message.getChannel().send().message(response);
                break;
            case "!displayQuote":
                if (messageArray.length==1){
                    //generate random quote
                    Random rnd = new Random();
                    int rando = rnd.nextInt(manager.getQuoteSize()-1)+1;
                    quote = manager.getQuote(message.getChannel().getName(),Integer.toString(rando));
                    message.getChannel().send().message("quote #"+rando+ " " +quote[0]+" -"+quote[1]);
                }else if (messageArray.length==2){
                    //return specific quote
                    quote = manager.getQuote(message.getChannel().getName(), messageArray[1]);
                    //message.respond(quote[0]+quote[1]);
                    message.getChannel().send().message("quote #"+messageArray[1]+ " " +quote[0]+" -"+quote[1]);
                }
                break;
            case "!quotes":
                if(message.getChannel().getOps().contains(message.getUser())){
                    if(messageArray[1].equals("off")){
                        quotesActive = false;
                    }
                    else if(messageArray[1].equals("on")){
                        quotesActive = true;
                    }
                }
                break;
        }

    }

    private String addQuote(String[] messageArray, String channel){
        String output = "";
        output += messageArray[2];
        for (int i = 3; i < messageArray.length; i++) {
            output += " " + messageArray[i];
        }
        int id = manager.getQuoteSize()+1;
        // build sql statement
        String statement = "INSERT INTO quotes ('num', 'quote', 'source', 'channel') VALUES( '" +id;
        statement += "', '" + output;
        statement += "', '" + messageArray[1];
        statement += "', '" + channel + "')";
        manager.connectToDatabase(statement);

        return ("Quote added successfully!");

    }

    private String removeQuote(int index, String channel){
        String statement = "DELETE FROM quotes WHERE num = '"+ index +"' AND channel = '" + channel +"';";
        manager.connectToDatabase(statement);
        return("Quote "+index+" removed successfully");

    }

    public void notify(String message) {
        if (message.equals("quotes")) {
            quotesActive = !quotesActive;
        }
    }
}
