package org.vertexarmy.hermes.client;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.vertexarmy.hermes.client.gui.ChatWindow;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * User: Alex
 * Date: 12/22/13
 */
public class Launcher {
    static {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException{

        final Properties properties = new Properties();
        properties.load(new FileInputStream("config.txt"));

        final Connection connection = new Connection(new HashMap<String, String>() {{
            for (String property : properties.stringPropertyNames()) {
                put(property, properties.getProperty(property));
            }
        }});
        connection.start();

        // initialize connection
        ChatWindow chatWindow = new ChatWindow(connection);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                connection.stop();
            }
        });
    }
}
