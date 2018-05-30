/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot.gui;

/**
 * @author JJ
 */
public interface GuiPublisher 
{   
    void broadcast(String message);
    void register(GuiSubscriber sub);
    void remove(GuiSubscriber sub);
}
