package com.pit.im.server.connector.impl;

import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.model.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * @author deng
 * @date 2020/1/2 17:47
 */
@Service
public class ImConnectorImpl implements ImConnector {
    @Override
    public void heartbeatToClient(ChannelHandlerContext hander, MessageWrapper wrapper) {

    }

    @Override
    public void pushMessage(MessageWrapper wrapper) throws RuntimeException {

    }

    @Override
    public void pushGroupMessage(MessageWrapper wrapper) throws RuntimeException {

    }

    @Override
    public boolean validateSession(MessageWrapper wrapper) throws RuntimeException {
        return false;
    }

    @Override
    public void close(ChannelHandlerContext hander, MessageWrapper wrapper) {

    }

    @Override
    public void close(String sessionId) {

    }

    @Override
    public void close(ChannelHandlerContext hander) {

    }

    @Override
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {

    }

    @Override
    public boolean exist(String sessionId) throws Exception {
        return false;
    }

    @Override
    public void pushMessage(String sessionId, MessageWrapper wrapper) throws RuntimeException {

    }

    @Override
    public String getChannelSessionId(ChannelHandlerContext ctx) {
        return null;
    }
}
