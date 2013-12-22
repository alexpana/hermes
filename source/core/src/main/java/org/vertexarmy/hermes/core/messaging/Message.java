package org.vertexarmy.hermes.core.messaging;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.experimental.Builder;

/**
 * User: Alex
 * Date: 12/22/13
 */
@Builder
public class Message implements Serializable {
    @Getter
    private final String from;
    @Getter
    private final String content;
    @Getter
    private final Date timestamp;
}
