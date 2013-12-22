package org.vertexarmy.hermes.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.vertexarmy.hermes.client.Connection;

/**
 * User: Alex
 * Date: 12/22/13
 */
public final class ChatWindow extends JFrame {
    private final Connection connection;
    private MessageBox messageBox;
    private JTextField replyTextField;
    private java.util.Timer timer;

    public ChatWindow(final Connection connection) {
        this.connection = connection;

        initComponents();
        initLayout();
        initListeners();

        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        messageBox.addMessages(connection.retrieveMessages());
                    }
                });
            }
        }, 0, 500);
    }

    private void initComponents() {
        messageBox = new PlainMessageBox();

        replyTextField = new JTextField();
    }

    private void initLayout() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.getInsets().set(3, 3, 3, 3);


        content.add(messageBox, BorderLayout.CENTER);
        content.add(replyTextField, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        getContentPane().add(content, BorderLayout.CENTER);

        pack();
        setSize(350, 400);
        // TODO: center on screen
        setVisible(true);

        replyTextField.requestFocus();
    }

    private void initListeners() {
        replyTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String message = actionEvent.getActionCommand();
                if (message == null || message.trim().length() == 0) {
                    return;
                }

                // send the message
                if (connection.sendMessage(message)) {
                    replyTextField.setText(null);
                }

                // ask for the unread messages
                messageBox.addMessages(connection.retrieveMessages());
            }
        });
    }
}
