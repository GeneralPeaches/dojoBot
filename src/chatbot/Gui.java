package chatbot;

/**
 * Created by slastic on 2/9/2015.
 */
import javax.swing.*;

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


        JCheckBox commands = new JCheckBox("commands", true);
        commands.addActionListener(listener);

        JCheckBox queue = new JCheckBox("queue", true);
        queue.addActionListener(listener);

        JCheckBox filter = new JCheckBox("filter");
        filter.addActionListener(listener);

            
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
        panel.add(filter);
        panel.add(commands);
        //panel.add(authenticate);
        guiFrame.add(panel);
        guiFrame.repaint();
        guiFrame.setVisible(true);
    }
}

