package chatbot.gui;

/**
 * Created by slastic on 2/9/2015.
 */
import javax.swing.*;

public class Gui 
{
    JTextField _channelName;
    GuiListener _listener;
    
    JFrame _guiFrame;
    JCheckBox _quote;
    JCheckBox _commands;
    JCheckBox _queue;
    JCheckBox _utility;
    JCheckBox _filter;

    public Gui()
    {
        _guiFrame = new JFrame();
        System.out.println("creating gui");

        //make sure the program exits when the frame closes
        _guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _guiFrame.setTitle("DojoBot");
        _guiFrame.setSize(300,250);
        _guiFrame.setLocationRelativeTo(null);
            
        JPanel panel = new JPanel();
        
        _channelName = new JTextField("Type Channel Name", 20);
        
        _listener = new GuiListener(_channelName, this);

        JButton close = new JButton("Close");
        close.addActionListener(_listener);

        _commands = new JCheckBox("commands", true);
        _commands.addActionListener(_listener);

        _queue = new JCheckBox("queue", true);
        _queue.addActionListener(_listener);
        
        _utility = new JCheckBox("utility", true);
        _utility.addActionListener(_listener);

        _filter = new JCheckBox("filter");
        _filter.addActionListener(_listener);
        
        _quote = new JCheckBox("quote", true);
        _quote.addActionListener(_listener);
            
        JButton connect = new JButton("Connect");
        connect.addActionListener(_listener);
        
        
        JButton authenticate = new JButton("Authenticate");
        authenticate.addActionListener(_listener);
        

        panel.add(_channelName);
        panel.add(connect);
        panel.add(close);
        panel.add(_queue);
        panel.add(_utility);
        panel.add(_quote);
        panel.add(_filter);
        panel.add(_commands);
        panel.add(authenticate);
        _guiFrame.add(panel);
        _guiFrame.repaint();
        _guiFrame.setVisible(true);
    }
}

