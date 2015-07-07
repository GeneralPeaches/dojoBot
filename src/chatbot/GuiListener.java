/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JTextField;
import org.pircbotx.Configuration;
import org.pircbotx.cap.EnableCapHandler;

/**
 *
 * @author JJ
 */
public class GuiListener implements ActionListener, GuiPublisher{
    private JTextField channelName;
    
    public GuiListener(JTextField channel){
        channelName = channel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command){
            case "Close":
                System.exit(0);
                break;
            case "Connect":
                String channel = channelName.getText();
                
                Commands com = new Commands();
                SpamControl sc = new SpamControl();
                ChatUtility cu = new ChatUtility();
                Quote qu = new Quote();
                
                final Configuration configuration = new Configuration.Builder()
                    .setName("dojobot") //Set the nick of the bot.
                    .setLogin("LQ") //login part of hostmask, egdd name:login@host
                    .setAutoNickChange(false) //Automatically change nick when the current one is in use
                    .setServer("irc.twitch.tv", 6667, "oauth:secret")
                    .addListener(com) //This class is a listener, so add it to the bots known listeners
                    .addListener(sc).addListener(cu).addListener(qu)
                    .addAutoJoinChannel("#" + channel) //Join the slastic channel
                    .buildConfiguration();

                Thread botThread = new Thread(){
                    public void run(){
                        new ChatBot(configuration);
                    }
                };
                botThread.start();
                
                register(com);
                register(sc);
                
                break;
                /*
            case "Authenticate":
                URI site = null;
                
                try {
                    site = new URI("www.google.com");
                } catch(URISyntaxException s) {
                    
                }
                
                Desktop window = Desktop.getDesktop();
                
                try{
                    window.browse(site);
                } catch (IOException i){
                    
                }
                
                
                break;*/
            default:
                broadcast(command);
                break;
        }
    }
    
    @Override
    public void register(GuiSubscriber sub){
        subscribers.add(sub);
    }
    
    @Override
    public void remove(GuiSubscriber sub){
        subscribers.remove(sub);
    }
    
    @Override
    public void broadcast(String message){
        for(GuiSubscriber sub:subscribers){
            sub.notify(message);
        }
    }
}
