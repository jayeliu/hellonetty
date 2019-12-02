package seus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler2Kafka extends ChannelInboundHandlerAdapter {
    MyKafkaProducer producer;
    ClientHandler2Kafka(MyKafkaProducer producer){
        this.producer=producer;
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
             producer.sendMsg(body);
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
