package seus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author jae-liu
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    LinkedBlockingDeque<String> responses = null;

    ServerHandler(LinkedBlockingDeque<String> responses) {
        this.responses = responses;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        /*ByteBuf buf=(ByteBuf)o;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req, StandardCharsets.UTF_8);*/
        String body = (String) o;
        System.out.println("Server:" + body);
        while (responses.isEmpty()){
            Thread.sleep(1000);
        }
        String response = responses.removeFirst();
        //System.out.println();
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
                //.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

        channelHandlerContext.close();
    }
}
