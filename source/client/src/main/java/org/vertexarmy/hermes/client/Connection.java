package org.vertexarmy.hermes.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.vertexarmy.hermes.core.messaging.Message;
import org.vertexarmy.hermes.core.net.Reply;
import org.vertexarmy.hermes.core.net.Request;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.zeromq.ZMQ.DONTWAIT;
import static org.zeromq.ZMQ.Socket;

/**
 * User: Alex
 * Date: 12/22/13
 */
public class Connection {
    private final ZContext context;
    private final Socket socket;
    private final String host;
    private final String clientUsername;
    private int lastReceivedMessage;

    public Connection(final Map<String, String> params) {
        context = new ZContext();
        socket = context.createSocket(ZMQ.REQ);

        host = params.get("host");
        clientUsername = params.get("username");

        lastReceivedMessage = -1;
    }

    public void start() {
        socket.connect(host);
    }

    public void stop() {
        socket.disconnect(host);
    }

    public boolean sendMessage(String message) {
        Request request = Request.builder()
                .type(Request.Type.POST_MESSAGE)
                .message(
                        Message.builder()
                                .from(clientUsername)
                                .content(message)
                                .build()
                )
                .build();

        sendRequest(request);
        Reply reply = awaitReply();

        return reply.getType() == Reply.Type.POST_MESSAGE && reply.isMessageReceived();

    }

    public List<Message> retrieveMessages() {
        Request request = Request.builder()
                .type(Request.Type.RETRIEVE_NEW_MESSAGES)
                .lastReceivedMessage(lastReceivedMessage)
                .build();

        sendRequest(request);
        Reply reply = awaitReply();

        lastReceivedMessage = reply.getLastReceivedMessage();
        return Arrays.asList(reply.getMessages());
    }

    public void statusUpdate() {
    }

    public boolean initializeConnection() {
        Request request = Request.builder()
                .type(Request.Type.INITIALIZE_CONNECTION)
                .build();

        sendRequest(request);
        Reply reply = awaitReply();
        return false;
    }

    private boolean sendRequest(Request request) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            socket.send(byteArrayOutputStream.toByteArray(), DONTWAIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Reply awaitReply() {
        try {
            byte[] replyBuffer = socket.recv();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(replyBuffer));
            return (Reply) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Reply awaitReply(int timeout) {
        try {
            byte[] replyBuffer = socket.recv();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(replyBuffer));
            return (Reply) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
