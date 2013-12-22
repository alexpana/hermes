package org.vertexarmy.hermes.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.vertexarmy.hermes.core.gui.Toolkit;
import org.vertexarmy.hermes.core.messaging.Message;

/**
 * User: Alex
 * Date: 12/22/13
 */
public class PlainMessageBox extends MessageBox {
    private JScrollPane scrollPane;
    private JTextArea textArea;

    public PlainMessageBox() {
        initComponents();
        initLayout();
        initListeners();
    }

    private void initComponents() {
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        Font messageFont = Toolkit.createFont(
                new String[] {"Lucida Grande", "Tahoma", "Verdana", "Arial", "Sans-Serif"}, Font.PLAIN, 13);
        textArea.setFont(messageFont);

        scrollPane = new JScrollPane(textArea);
    }

    private void initLayout() {
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                scrollToBottom();
            }
        });
    }

    public void addMessage(Message message) {
        textArea.setCaretPosition(textArea.getDocument().getLength());
        textArea.append(message.getFrom() + ": " + message.getContent() + "\n");
        scrollToBottom();
    }

    public void addMessages(java.util.List<Message> messageList) {
        for (Message message : messageList) {
            addMessage(message);
        }
    }

    private void scrollToBottom() {
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
}
