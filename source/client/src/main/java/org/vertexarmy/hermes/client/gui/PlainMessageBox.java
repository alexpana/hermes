package org.vertexarmy.hermes.client.gui;

import java.awt.BorderLayout;
import java.util.Date;
import javax.swing.JTextArea;
import org.vertexarmy.hermes.core.messaging.Message;

/**
 * User: Alex
 * Date: 12/22/13
 */
public class PlainMessageBox extends MessageBox {
    private JTextArea textArea;

    public PlainMessageBox() {
        textArea = new JTextArea();

        this.setLayout(new BorderLayout());
        this.add(textArea, BorderLayout.CENTER);
    }

    public void addMessage(Message message) {
        textArea.append(message.getFrom() + ": " + message.getContent() + "\n");
    }

    public void addMessages(java.util.List<Message> messageList) {
        for (Message message : messageList) {
            addMessage(message);
        }
    }

    private String formatTimestamp(Date timestamp) {
        return String.format("[%d:%d:%d]", 0, 0, 0);
    }
}
