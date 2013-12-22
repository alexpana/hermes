package org.vertexarmy.hermes.server;

import org.vertexarmy.hermes.core.messaging.Message;
import org.vertexarmy.hermes.core.net.Reply;
import org.vertexarmy.hermes.core.net.Request;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Alex
 * Date: 12/22/13
 */
public class Launcher {
    private static final String BIND_ADDRESS = "tcp://*:4269";
    private static ZMQ.Socket socket;
    private static List<Message> messages;

    public static void main(String args[]) {
        messages = new ArrayList<Message>();

        ZContext context = new ZContext();

        socket = context.createSocket(ZMQ.REP);
        int result = socket.bind(BIND_ADDRESS);

        if (result != -1) {
            System.out.println("Hermes server started on " + BIND_ADDRESS);
        } else {
            System.out.println("Hermes server could not start on " + BIND_ADDRESS);
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            Request request = acceptRequest();
            processRequest(request);
        }

        // cleanup
        socket.close();
        context.close();
    }

    private static void processRequest(final Request request) {
        if (request == null) {
            return;
        }
        Reply reply = null;

        switch (request.getType()) {
            case INITIALIZE_CONNECTION:
                // TODO: check if BIND_ADDRESS is possible

                reply = Reply.builder()
                        .type(Reply.Type.INITIALIZE_CONNECTION)
                        .build();
                break;

            case POST_MESSAGE:
                messages.add(request.getMessage());

                reply = Reply.builder()
                        .type(Reply.Type.POST_MESSAGE)
                        .messageReceived(true)
                        .build();
                break;

            case RETRIEVE_NEW_MESSAGES:
                int unreadMessagesCount = messages.size() - request.getLastReceivedMessage() - 1;
                Message[] messageArray = new Message[unreadMessagesCount];
                messages.subList(request.getLastReceivedMessage() + 1, messages.size()).toArray(messageArray);
                reply = Reply.builder()
                        .type(Reply.Type.RETRIEVE_NEW_MESSAGES)
                        .messages(messageArray)
                        .lastReceivedMessage(messages.size() - 1)
                        .build();
                break;

            case STATUS_UPDATE:
                // TODO: check for status of other peers

                reply = Reply.builder()
                        .type(Reply.Type.STATUS_UPDATE)
                        .build();
                break;
        }

        sendReply(reply);
    }

    private static void sendReply(Reply reply) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(reply);
            socket.send(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Request acceptRequest() {
        try {
            byte[] replyBuffer = socket.recv();
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(replyBuffer));
            return (Request) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
