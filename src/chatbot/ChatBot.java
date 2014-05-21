/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.*;

/**
 *
 * @author JJ
 */
public class ChatBot extends PircBotX{
    
    public ChatBot(Configuration config){
        super(config);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        //I want to have the bot join the appropriate channel
        //immediately when run, but I don't think that's feasible.
       
        //creates the configuration for the bot to use
        Configuration configuration = new Configuration.Builder()
            .setName("dojobot") //Set the nick of the bot. CHANGE IN YOUR CODE
            .setLogin("LQ") //login part of hostmask, eg name:login@host
            .setAutoNickChange(false) //Automatically change nick when the current one is in use
            .setCapEnabled(true) //Enable CAP features
            .addListener(new Commands()) //This class is a listener, so add it to the bots known listeners
            .setServer("irc.twitch.tv", 6667, "oauth:onhold")
            .addAutoJoinChannel("slastic") //Join the slastic channel
            .buildConfiguration();
        
        //creates the bot
        ChatBot bot = new ChatBot(configuration);
        
        //connect to server
        try {
            bot.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /*
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String response = commands.getResponse(channel, sender, login, hostname, message);

        sendMessage(channel, response);
        //sendMessage(channel, response);
    }
    
    //*/
    //TODO: Various other commands
}
