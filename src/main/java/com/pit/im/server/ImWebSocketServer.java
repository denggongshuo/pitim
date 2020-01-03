package com.pit.im.server;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.pit.im.constants.ImConstants;
import com.pit.im.server.connector.ImConnector;
import com.pit.im.server.handler.ImWebSocketServer.ImWebSocketServerHandler;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.proxy.MessageProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * @author deng
 * @date 2020/1/3 13:52
 */
@Component
@Slf4j
public class ImWebSocketServer {

    @Value("${im.webSocketServer.port}")
    private int port;

    private ProtobufDecoder decoder = new ProtobufDecoder(MessageProto.Model.getDefaultInstance());

    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private ImConnector connector;

    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public void start() {
        log.info("start im websocketserver ...");
        // Server 服务启动
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // HTTP请求的解码和编码
                pipeline.addLast(new HttpServerCodec());
                // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
                // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
                pipeline.addLast(new HttpObjectAggregator(ImConstants.ImserverConfig.MAX_AGGREGATED_CONTENT_LENGTH));
                // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
                pipeline.addLast(new ChunkedWriteHandler());
                // WebSocket数据压缩
                pipeline.addLast(new WebSocketServerCompressionHandler());
                // 协议包长度限制
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null,
                        true, ImConstants.ImserverConfig.MAX_FRAME_LENGTH));
                // 协议包解码
                pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
                        ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
                        objs.add(buf);
                        buf.retain();
                    }
                });
                // 协议包编码
                pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
                    @Override
                    protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
                        ByteBuf result = null;
                        if (msg instanceof MessageLite) {
                            result = wrappedBuffer(((MessageLite) msg).toByteArray());
                        }
                        if (msg instanceof MessageLite.Builder) {
                            result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
                        }
                        // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
                        WebSocketFrame frame = new BinaryWebSocketFrame(result);
                        out.add(frame);
                        // 协议包解码时指定Protobuf字节数实例化为CommonProtocol类型
                        pipeline.addLast(decoder);
                        pipeline.addLast(new IdleStateHandler(ImConstants.ImserverConfig.READ_IDLE_TIME, ImConstants.ImserverConfig.WRITE_IDLE_TIME, 0));
                        // 业务处理器
                        pipeline.addLast(new ImWebSocketServerHandler(messageProxy,connector));
                    }
                });
                // 可选参数
                bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
                // 绑定接口，同步等待成功
                log.info("start im websocketserver at port[" + port + "].");
                ChannelFuture future = bootstrap.bind(port).sync();
                channel = future.channel();
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            log.info("websocketserver have success bind to " + port);
                        } else {
                            log.error("websocketserver fail bind to " + port);
                        }
                    }
                });

            }
        });

    }

    @PreDestroy
    public void destroy() {
        log.info("destroy qiqiim websocketserver ...");
        // 释放线程池资源
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("destroy qiqiim webscoketserver complate.");
    }

}
