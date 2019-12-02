package seus;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Client2Kafka {
    MyKafkaProducer producer=new MyKafkaProducer("test");
    private Client2Kafka() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup requestGroup = new NioEventLoopGroup();
        bootstrap.group(requestGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //socketChannel.pipeline().addLast(new ReadTimeoutHandler(20));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ClientHandler2Kafka(producer));
                    }
                });
        ChannelFuture channelFuture= bootstrap.connect("localhost",8765).sync();
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("Hello!".getBytes()));
        channelFuture.channel().closeFuture().sync();
        requestGroup.shutdownGracefully();
    }
    public static void main(String[] args) throws Exception{
        new Client2Kafka();
    }
}
