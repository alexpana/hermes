package org.vertexarmy.hermes.client.gui;

import org.vertexarmy.hermes.core.messaging.Message;

import javax.swing.*;
import java.util.List;

/**
 * User: Alex
 * Date: 12/22/13
 */
public abstract class MessageBox extends JPanel{
    public abstract void addMessage(Message message);
    public abstract void addMessages(List<Message> messageList);
}
