package org.vertexarmy.hermes.client.gui;

import org.vertexarmy.hermes.client.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * User: Alex
 * Date: 12/22/13
 */
public final class ChatWindow extends JFrame {
    private MessageBox messageBox;
    private JTextField replyTextField;
    private final Connection connection;
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
        setTitle("Hermes Client [alpha]");
        messageBox = new PlainMessageBox();

        replyTextField = new JTextField();
    }

    private void initLayout() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.getInsets().set(3, 3, 3, 3);

        content.add(new JScrollPane(messageBox), BorderLayout.CENTER);
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
                if (connection.sendMessage(message)) {
                    replyTextField.setText(null);
                }

                messageBox.addMessages(connection.retrieveMessages());
            }
        });
    }
}