package part1.common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import part1.common.Message.MessageType;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;
import part1.common.serializer.Serializer;

/*
    @author 张星宇
 */
@AllArgsConstructor
public class MyEncode extends MessageToByteEncoder {
    private Serializer serializer;
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Object message, ByteBuf out) throws Exception {
        System.out.println(message.getClass());
        if(message instanceof RpcRequest) out.writeShort(MessageType.REQUEST.getCode());
        else if(message instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        out.writeShort(serializer.getType());
        System.out.println(serializer.getType());
        byte[] seriliazeBytes = serializer.serialize(message);
        out.writeInt(seriliazeBytes.length);
        System.out.println(seriliazeBytes.length);
        out.writeBytes(seriliazeBytes);
        System.out.println("完成编码");

    }
}
