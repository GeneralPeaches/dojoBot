/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import org.pircbotx.Configuration;

/**
 *
 * @author JJ
 */
public class GuiListener implements ActionListener{
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
                String channel = channelName.getText().toString();
                final Configuration configuration = new Configuration.Builder()
                    .setName("dojobot") //Set the nick of the bot.
                    .setLogin("LQ") //login part of hostmask, eg name:login@host
                    .setAutoNickChange(false) //Automatically change nick when the current one is in use
                    .setCapEnabled(false) //Enable CAP features
                    .addListener(new Commands()) //This class is a listener, so add it to the bots known listeners
                    .addListener(new SpamControl())
                    .setServer("irc.twitch.tv", 6667, "oauth:secret")
                    .addAutoJoinChannel("#" + channel) //Join the slastic channel
                    .buildConfiguration();

                Thread botThread = new Thread(){
                    public void run(){
                        new ChatBot(configuration);
                    }
                };
                botThread.start();
                break;
            default:
                break;
        }
    }
}
