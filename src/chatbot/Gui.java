package chatbot;

/**
 * Created by slastic on 2/9/2015.
 */
import javax.swing.*;

public class Gui {
    JTextField channelName;
    GuiListener listener;
    
    JFrame guiFrame;
    JCheckBox quote;
    JCheckBox commands;
    JCheckBox queue;
    JCheckBox utility;
    JCheckBox filter;
    

    public Gui()
    {
        guiFrame = new JFrame();
        System.out.println("creating gui");

        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("DojoBot");
        guiFrame.setSize(300,250);
        guiFrame.setLocationRelativeTo(null);
            
        JPanel panel = new JPanel();
        
        channelName = new JTextField("Type Channel Name", 20);
        
        listener = new GuiListener(channelName, this);

        JButton close = new JButton("Close");
        close.addActionListener(listener);

        commands = new JCheckBox("commands", true);
        commands.addActionListener(listener);

        queue = new JCheckBox("queue", true);
        queue.addActionListener(listener);
        
        utility = new JCheckBox("utility", true);
        utility.addActionListener(listener);

        filter = new JCheckBox("filter");
        filter.addActionListener(listener);
        
        quote = new JCheckBox("quote", true);
        quote.addActionListener(listener);
            
        JButton connect = new JButton("Connect");
        connect.addActionListener(listener);
        
        /*
        JButton authenticate = new JButton("Authenticate");
        authenticate.addActionListener(listener);
        */

        panel.add(channelName);
        panel.add(connect);
        panel.add(close);
        panel.add(queue);
        panel.add(utility);
        panel.add(quote);
        panel.add(filter);
        panel.add(commands);
        //panel.add(authenticate);
        guiFrame.add(panel);
        guiFrame.repaint();
        guiFrame.setVisible(true);
    }
}

