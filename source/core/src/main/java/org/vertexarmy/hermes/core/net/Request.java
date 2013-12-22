package org.vertexarmy.hermes.core.net;

import lombok.Getter;
import lombok.experimental.Builder;
import org.vertexarmy.hermes.core.messaging.Message;

import java.io.Serializable;

/**
 * User: Alex
 * Date: 12/22/13
 */
@Builder
public final class Request implements Serializable {
    @Getter
    private final String source;
    @Getter
    private final Type type;

    // POST_MESSAGE
    @Getter
    private final Message message;

    // RETRIEVE_NEW_MESSAGES
    @Getter
    private final int lastReceivedMessage;

    public enum Type {
        INITIALIZE_CONNECTION,
        POST_MESSAGE,
        RETRIEVE_NEW_MESSAGES,
        STATUS_UPDATE
    }
}
