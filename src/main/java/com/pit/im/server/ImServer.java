package com.pit.im.server;

import com.pit.im.constants.ImConstants;
import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.exception.InitErrorException;
import com.pit.im.server.handler.ImServer.ImServerHandler;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.proxy.MessageProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author deng
 * @date 2019/12/31 13:35
 */
public class ImServer {
    private final static Logger log = LoggerFactory.getLogger(ImServer.class);


    private int port;


    private ProtobufDecoder decoder = new ProtobufDecoder(MessageProto.Model.getDefaultInstance());
    private ProtobufEncoder encoder = new ProtobufEncoder();

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private ImConnector connector;


    public void start() throws Exception {
        log.info("start im server ...");
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workerGroup, bossGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                pipeline.addLast("decoder", decoder);
                pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast("encoder", encoder);
                pipeline.addLast(new IdleStateHandler(ImConstants.ImserverConfig.READ_IDLE_TIME, ImConstants.ImserverConfig.WRITE_IDLE_TIME, 0));
                pipeline.addLast("handler", new ImServerHandler(messageProxy, connector));
            }
        });
        // 可选参数
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        // 绑定接口，同步等待成功
        log.info("start im server at port[{}].", port);
        ChannelFuture future = bootstrap.bind(port).sync();
        channel = future.channel();
        future.addListener(i -> {
            if (future.isSuccess()) {
                log.info("Server have success bind to " + port);
            } else {
                log.error("Server fail bind to " + port);
                throw new InitErrorException("Server start fail !", future.cause());
            }

        });
    }


    public void destroy() {
        log.info("destroy ImServer ...");
        // 释放线程池资源
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("destroy ImServer complate.");
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMessageProxy(MessageProxy messageProxy) {
        this.messageProxy = messageProxy;
    }

    public void setConnector(ImConnector connector) {
        this.connector = connector;
    }
}
