/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatbot;

import org.pircbotx.*;

/**
 *
 * @author General Peaches and Slastic
 */
public class ChatBot extends PircBotX{
    
    public ChatBot(Configuration config){

        super(config);
        try {
            this.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
