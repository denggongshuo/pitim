package com.pit.im.server.handler.ImServer;

import cn.hutool.core.util.StrUtil;
import com.pit.im.constants.ImConstants;
import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.model.MessageWrapper;
import com.pit.im.server.proxy.MessageProxy;
import com.pit.im.utils.ImUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * @author deng
 * @date 2020/1/2 17:50
 */
@Sharable
@Slf4j
public class ImServerHandler extends ChannelInboundHandlerAdapter {

    private ImConnector connector = null;

    private MessageProxy messageProxy = null;


    public ImServerHandler(MessageProxy messageProxy, ImConnector connector) {
        this.connector = connector;
        this.messageProxy = messageProxy;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String sessionId = ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_ID).get();
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.WRITER_IDLE)) {
            //发送心跳包
            if (StrUtil.isNotEmpty(sessionId)) {
                MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
                builder.setCmd(ImConstants.CmdType.HEARTBEAT);
                builder.setMsgtype(ImConstants.ProtobufType.SEND);
                ctx.channel().writeAndFlush(builder);
            }
            log.debug(IdleState.WRITER_IDLE + "... from " + sessionId + "-->" + ctx.channel().remoteAddress() + " nid:" + ctx.channel().id().asShortText());

        }
        //如果心跳请求发出70秒内没收到响应，则关闭连接
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {
            log.debug(IdleState.READER_IDLE + "... from " + sessionId + " nid:" + ctx.channel().id().asShortText());
            Long lastTime = (Long) ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_HEARBEAT).get();
            Long currentTime = System.currentTimeMillis();
            if (lastTime == null || ((currentTime - lastTime) / 1000 >= ImConstants.ImserverConfig.PING_TIME_OUT)) {
                connector.close(ctx);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof MessageProto.Model) {
                MessageProto.Model message = (MessageProto.Model) msg;
                String sessionId = connector.getChannelSessionId(ctx);
                // inbound
                if (message.getMsgtype() == ImConstants.ProtobufType.SEND) {
                    ctx.channel().attr(ImConstants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
                    MessageWrapper wrapper = messageProxy.convertToMessageWrapper(sessionId, message);
                    if (wrapper != null)
                        receiveMessages(ctx, wrapper);
                }
                //outbound
                if (message.getMsgtype() == ImConstants.ProtobufType.REPLY) {
                    MessageWrapper wrapper = messageProxy.convertToMessageWrapper(sessionId, message);
                    if (wrapper != null)
                        receiveMessages(ctx, wrapper);
                }

            } else {
                log.warn("ImServerHandler channelRead message is not proto.");
            }
        } catch (Exception e) {
            log.error("ImServerHandler channerRead error.", e);
            throw e;
        }

    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("ImServerHandler  join from "+ ImUtils.getRemoteAddress(ctx)+" nid:" + ctx.channel().id().asShortText());
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("ImServerHandler Disconnected from {" +ctx.channel().remoteAddress()+"--->"+ ctx.channel().localAddress() + "}");
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("ImServerHandler channelActive from (" + ImUtils.getRemoteAddress(ctx) + ")");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("ImServerHandler channelInactive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        String sessionId = connector.getChannelSessionId(ctx);
        receiveMessages(ctx,new MessageWrapper(MessageWrapper.MessageProtocol.CLOSE, sessionId,null, null));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ImServerHandler (" + ImUtils.getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
    }

    /**
     * to send  message
     *
     * @param hander
     * @param wrapper
     */
    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
        //设置消息来源为socket
        wrapper.setSource(ImConstants.ImserverConfig.SOCKET);
        if (wrapper.isConnect()) {
            connector.connect(hander, wrapper);
        } else if (wrapper.isClose()) {
            connector.close(hander, wrapper);
        } else if (wrapper.isHeartbeat()) {
            connector.heartbeatToClient(hander, wrapper);
        } else if (wrapper.isGroup()) {
            connector.pushGroupMessage(wrapper);
        } else if (wrapper.isSend()) {
            connector.pushMessage(wrapper);
        } else if (wrapper.isReply()) {
            connector.pushMessage(wrapper.getSessionId(), wrapper);
        }
    }
}

