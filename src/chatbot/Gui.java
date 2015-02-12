package chatbot;

/**
 * Created by slastic on 2/9/2015.
 */


//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import org.pircbotx.Configuration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Gui {
    JTextField channelName;
    GuiListener listener;

    public Gui()
    {
        JFrame guiFrame = new JFrame();
        System.out.println("creating gui");

        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("DojoBot");
        guiFrame.setSize(300,250);
        guiFrame.setLocationRelativeTo(null);
            
        JPanel panel = new JPanel();
        
        channelName = new JTextField("Type Channel Name", 20);
        
        listener = new GuiListener(channelName);

        JButton close = new JButton("Close");
        close.addActionListener(listener);
            
        JButton connect = new JButton("Connect");
        connect.addActionListener(listener);

        panel.add(channelName);
        panel.add(connect);
        panel.add(close);
        guiFrame.add(panel);
        guiFrame.repaint();
        guiFrame.setVisible(true);
    }

    /*
    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    protected class ConnectListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
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

        }
    }*/
}

