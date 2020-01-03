package com.pit.im.server.handler.ImWebSocketServer;

import com.pit.im.constants.ImConstants;
import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.model.MessageWrapper;
import com.pit.im.server.proxy.MessageProxy;
import com.pit.im.utils.ImUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author deng
 * @date 2020/1/2 17:51
 */
@Slf4j
@ChannelHandler.Sharable
public class ImWebSocketServerHandler extends SimpleChannelInboundHandler<MessageProto.Model> {
    private MessageProxy messageProxy = null;
    private ImConnector connector = null;

    public ImWebSocketServerHandler() {
    }

    public ImWebSocketServerHandler(MessageProxy messageProxy, ImConnector connector) {
        this.messageProxy = messageProxy;
        this.connector = connector;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object o) throws Exception {
        String sessionId = ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_ID).get();
        //发送心跳包
        if (o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.WRITER_IDLE)) {
            if (StringUtils.isNotEmpty(sessionId)) {
                MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
                builder.setCmd(ImConstants.CmdType.HEARTBEAT);
                builder.setMsgtype(ImConstants.ProtobufType.SEND);
                ctx.channel().writeAndFlush(builder);
            }
            log.debug(IdleState.WRITER_IDLE + "... from " + sessionId + "-->" + ctx.channel().remoteAddress() + " nid:" + ctx.channel().id().asShortText());
        }

        //如果心跳请求发出70秒内没收到响应，则关闭连接
        if (o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.READER_IDLE)) {
            log.debug(IdleState.READER_IDLE + "... from " + sessionId + " nid:" + ctx.channel().id().asShortText());
            Long lastTime = (Long) ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_HEARBEAT).get();
            if (lastTime == null || ((System.currentTimeMillis() - lastTime) / 1000 >= ImConstants.ImserverConfig.PING_TIME_OUT)) {
                connector.close(ctx);
            }

        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProto.Model msg) throws Exception {
        try {
            String sessionId = connector.getChannelSessionId(ctx);
            // inbound
            if (msg.getMsgtype() == ImConstants.ProtobufType.SEND) {
                ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
                MessageWrapper wrapper = messageProxy.convertToMessageWrapper(sessionId, msg);
                if (wrapper != null)
                    receiveMessages(ctx, wrapper);
            }
            // outbound
            if (msg.getMsgtype() == ImConstants.ProtobufType.REPLY) {
                MessageWrapper wrapper = messageProxy.convertToMessageWrapper(sessionId, msg);
                if (wrapper != null)
                    receiveMessages(ctx, wrapper);
            }
        } catch (Exception e) {
            log.error("ImWebSocketServerHandler channerRead error.", e);
            throw e;
        }
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("ImWebSocketServerHandler  join from " + ImUtils.getRemoteAddress(ctx) + " nid:" + ctx.channel().id().asShortText());
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("ImWebSocketServerHandler Disconnected from {" + ctx.channel().remoteAddress() + "--->" + ctx.channel().localAddress() + "}");
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("ImWebSocketServerHandler channelActive from (" + ImUtils.getRemoteAddress(ctx) + ")");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("ImWebSocketServerHandler channelInactive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        String sessionId = connector.getChannelSessionId(ctx);
        receiveMessages(ctx, new MessageWrapper(MessageWrapper.MessageProtocol.CLOSE, sessionId, null, null));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ImWebSocketServerHandler (" + ImUtils.getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
    }

    private void receiveMessages(ChannelHandlerContext ctx, MessageWrapper wrapper) {

        //设置消息来源为Websocket
        wrapper.setSource(ImConstants.ImserverConfig.WEBSOCKET);
        if (wrapper.isConnect()) {
            connector.connect(ctx, wrapper);
        } else if (wrapper.isClose()) {
            connector.close(ctx, wrapper);
        } else if (wrapper.isHeartbeat()) {
            connector.heartbeatToClient(ctx, wrapper);
        } else if (wrapper.isGroup()) {
            connector.pushGroupMessage(wrapper);
        } else if (wrapper.isSend()) {
            connector.pushMessage(wrapper);
        } else if (wrapper.isReply()) {
            connector.pushMessage(wrapper.getSessionId(), wrapper);
        }
    }


}
