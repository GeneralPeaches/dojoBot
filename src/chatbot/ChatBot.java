/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.io.IOException;
import java.util.ArrayList;
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
            bot.connect("irc.twitch.tv", 6667,"oath:PLACEHOLDER" );
        } catch (IOException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IrcException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //join the #pircbot channel
        bot.joinChannel("#slastic");



    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        //System.out.println(sender);
        ArrayList<String> mods = mods(channel);
        String response = commands.getResponse(channel, sender, login, hostname, message, mods);
       
        sendMessage(channel, response);
    }

    public ArrayList<String> mods(String channel){
        User[] users = getUsers(channel);


        ArrayList<String> mods = new ArrayList<String>();
        for(User i : users){
            System.out.println(i.toString());
            //System.out.println(i.getPrefix());
            if(i.isOp()){
                mods.add(i.getNick());
            }
        }
        return mods;

    }
    //TODO: Various other commands
}
