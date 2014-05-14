/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.*;

/**
 *
 * @author JJ
 */
public class ChatBot extends PircBot{

    private final Commands commands = new Commands();
    
    public ChatBot() {
        this.setName("dojobot");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //I want to have the bot join the appropriate channel
        //immediately when run, but I don't think that's feasible.
        //creates bot
        ChatBot bot = new ChatBot();
        
        //enable debugging output
        bot.setVerbose(true);
        try {
            //connect to the IRC server
            bot.connect("irc.twitch.tv");
        } catch (IOException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IrcException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //join the #pircbot channel
        bot.joinChannel("#pircbot");
    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String response = commands.getResponse(channel, sender, login, hostname, message);
       
        sendMessage(channel, response);
    }
    
    //TODO: Various other commands
}
