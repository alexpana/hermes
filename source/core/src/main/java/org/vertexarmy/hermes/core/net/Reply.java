package org.vertexarmy.hermes.core.net;

import java.io.Serializable;
import lombok.Getter;
import lombok.experimental.Builder;
import org.vertexarmy.hermes.core.messaging.Message;

/**
 * User: Alex
 * Date: 12/22/13
 */
@Builder
public final class Reply implements Serializable {
    @Getter
    private final Type type;
    // POST_MESSAGE
    @Getter
    private final boolean messageReceived;
    // RETRIEVE_MESSAGES
    @Getter
    private final Message[] messages;
    @Getter
    private final int lastReceivedMessage;
    // STATUS_UPDATE
    @Getter
    private final boolean messageReceivedByPeer;
    @Getter
    private final boolean peerResponding;
    @Getter
    private final boolean peerNotResponding;

    public static enum Type {
        INITIALIZE_CONNECTION,
        POST_MESSAGE,
        RETRIEVE_NEW_MESSAGES,
        STATUS_UPDATE
    }
}
