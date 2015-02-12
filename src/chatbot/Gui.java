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

            JButton close = new JButton("Close");
            
            JPanel panel = new JPanel();

            close.setSize(100,85);
            
            close.addActionListener(new CloseListener());
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

