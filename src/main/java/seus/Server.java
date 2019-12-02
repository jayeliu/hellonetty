package seus;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.commons.logging.LogSource;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author jae-liu
 */
public class Server{
    LinkedBlockingDeque<String> responses;
    Server(LinkedBlockingDeque<String> responses){
        this.responses=responses;
    }
    void service() {
        //deal with connect
        EventLoopGroup poolGroup = new NioEventLoopGroup();
        //deal with transmission
        EventLoopGroup transmitGroup = new NioEventLoopGroup();
        //Bootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(poolGroup, transmitGroup)
                /*channel type*/
                .channel(NioServerSocketChannel.class)
                //tcp buf size
                .option(ChannelOption.SO_BACKLOG, 1024)
                //receivebuf size
                .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        /*ByteBuf buf= Unpooled.copiedBuffer("$_".getBytes());
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
                        */
                        //socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(5));

                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ServerHandler(responses));
                    }
                });
        //bind port
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.bind(8765).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         /*try {
             //NIO close
             assert channelFuture != null;
             channelFuture.channel().closeFuture().sync();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }*/
        poolGroup.shutdownGracefully();
        transmitGroup.shutdownGracefully();
    }
}
class Crawler implements Runnable{
    static final Logger logger = LoggerFactory.getLogger(Server.class);
    List<String> urls;
    LinkedBlockingDeque<String> responses;
    Crawler(List<String> urls,LinkedBlockingDeque<String> responses){
        this.urls=urls;
        this.responses=responses;
    }
    private void crawler() throws IOException, InterruptedException {
        for (String url : urls) {
            String html=MyHttpClient.getHtml(url);
            String text=MyHttpClient.getText(html);
            if (text!=null){
                responses.addFirst(text);
            }
            //System.out.println(MyHttpClient.getUrl(html));
            //System.out.println(responses.peek());
            //Thread.sleep(200);
        }
        logger.info("ending time:"+String.valueOf(Instant.now()));
    }

    @Override
    public void run() {
        try {
            crawler();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class BasicThread {
    public static void main(String[] args){
        LinkedBlockingDeque<String> responses = new LinkedBlockingDeque<>();
        List<String> stringList=TxtRead.readTxtFileIntoStringArrList("url.txt");
        System.out.println("end reading");
        Server server = new Server(responses);
        int num=stringList.size()/20;
        ExecutorService exec= Executors.newCachedThreadPool();
        //Thread thread = new Thread(server);
        //thread.start();
       for (int i=0;i<20;i++){
           if (i ==19) {
               exec.execute(new Crawler(stringList.subList(i * num, stringList.size()),responses));
           } else {
               exec.execute(new Crawler(stringList.subList(i * num, (i + 1) * num),responses));
           }
        }
        server.service();
        exec.shutdown();
    }
}