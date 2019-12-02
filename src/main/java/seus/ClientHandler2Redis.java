package seus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.kafka.common.protocol.types.Field;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientHandler2Redis extends ChannelInboundHandlerAdapter {
    static int num=1;
    static Jedis jedis = new Jedis("localhost");
    static void redisClient(String value){
        jedis.set(String.valueOf(num),value);
        num += 1;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
             /*ByteBuf buf=(ByteBuf)msg;
             byte[] req=new byte[buf.readableBytes()];
             buf.readBytes(req);
             String body=new String(req,"utf-8");*/
            String body=(String)msg;
            System.out.println("Receive from Server!");
            redisClient(body);
            ctx.writeAndFlush(Unpooled.copiedBuffer("Received!".getBytes()));
        }finally {
            ReferenceCountUtil.release(msg);//import! must release msg

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
