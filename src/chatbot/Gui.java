package chatbot;

/**
 * Created by slastic on 2/9/2015.
 */


//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Gui {




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

            JButton close = new JButton("Close");
            close.addActionListener(new CloseListener());
            
            JTextField channelName = new JTextField("Type Channel Name", 20);
            
            JButton connect = new JButton("Connect");
            //connect.addActionListener(new ConnectListener);
            
            panel.add(channelName);
            panel.add(connect);
            panel.add(close);
            guiFrame.add(panel);
            guiFrame.repaint();
            guiFrame.setVisible(true);






        }

        private class CloseListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

    }

