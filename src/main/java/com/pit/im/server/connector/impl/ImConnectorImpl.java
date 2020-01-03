package com.pit.im.server.connector.impl;

import com.pit.im.constants.ImConstants;
import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.exception.PushException;
import com.pit.im.server.group.ImChannelGroup;
import com.pit.im.server.model.MessageWrapper;
import com.pit.im.server.model.Session;
import com.pit.im.server.model.session.SessionManger;
import com.pit.im.server.proxy.MessageProxy;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author deng
 * @date 2020/1/2 17:47
 */
@Service
@Slf4j
public class ImConnectorImpl implements ImConnector {
    @Autowired
    private MessageProxy messageProxy;

    @Autowired
    private SessionManger sessionManger;

    @Override
    public void heartbeatToClient(ChannelHandlerContext hander, MessageWrapper wrapper) {
        hander.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
    }

    @Override
    public void pushMessage(MessageWrapper wrapper) throws RuntimeException {
        Session session = sessionManger.getSession(wrapper.getSessionId());
        try {
            /*
             * 服务器集群时，可以在此
             * 判断当前session是否连接于本台服务器，如果是，继续往下走，如果不是，将此消息发往当前session连接的服务器并 return
             * if(session!=null&&!session.isLocalhost()){//判断当前session是否连接于本台服务器，如不是
             * //发往目标服务器处理
             * return;
             * }
             */
            if (null != session) {
                session.write(wrapper.getBody());
            }
        } catch (Exception e) {
            log.error("connector pushMessage  Exception.", e);
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void pushMessage(String sessionId, MessageWrapper wrapper) throws RuntimeException {
        ///取得接收人 给接收人写入消息
        try {
            Session responseSession = sessionManger.getSession(wrapper.getReSessionId());
            if (null != responseSession && responseSession.isConnected()) {
                boolean result = responseSession.write(wrapper.getBody());
                if (result) {
                    messageProxy.saveOnlineMessageToDB(wrapper);
                } else {
                    messageProxy.saveOfflineMessageToDB(wrapper);
                }
                return;
            } else {
                messageProxy.saveOfflineMessageToDB(wrapper);
            }
        } catch (PushException e) {
            log.error("connector send occur PushException.", e);

            throw new RuntimeException(e.getCause());
        } catch (Exception e) {
            log.error("connector send occur Exception.", e);
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void pushGroupMessage(MessageWrapper wrapper) throws RuntimeException {
        //这里判断群组ID 是否存在 并且该用户是否在群组内
        ImChannelGroup.broadcast(wrapper.getBody());
        messageProxy.saveOnlineMessageToDB(wrapper);
    }

    @Override
    public boolean validateSession(MessageWrapper wrapper) throws RuntimeException {
        try {
            return sessionManger.exist(wrapper.getSessionId());
        } catch (Exception e) {
            log.error("connector validateSession Exception!", e);
            throw new RuntimeException(e.getCause());
        }
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
    public String getChannelSessionId(ChannelHandlerContext ctx) {
        return null;
    }
}
