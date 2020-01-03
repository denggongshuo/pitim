package com.pit.im.server.handler.ImServer;

import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.proxy.MessageProxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @author deng
 * @date 2020/1/2 17:50
 */
@Sharable
public class ImServerHandler extends ChannelInboundHandlerAdapter {

    private ImConnector connector = null;
    private MessageProxy messageProxy = null;


    public ImServerHandler(MessageProxy messageProxy, ImConnector connector) {
        this.connector = connector;
        this.messageProxy = messageProxy;
    }

    //用户注册
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
