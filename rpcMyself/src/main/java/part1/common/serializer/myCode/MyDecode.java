package part1.common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import part1.common.Message.MessageType;
import part1.common.serializer.Serializer;

import java.util.List;

/*
    @author 张星宇
 */
@AllArgsConstructor
public class MyDecode extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> out) throws Exception {
        short messageType=byteBuf.readShort();
        if(messageType!= MessageType.REQUEST.getCode() && messageType!= MessageType.RESPONSE.getCode()){
            System.out.println("messageType is not right");
            return;
        }
        short serializerType=byteBuf.readShort();
        if(serializerType!=0 && serializerType!=1){
            System.out.println("serializerType is not right");
            return;
        }
        Serializer serializer=Serializer.getSerializerByCode(serializerType);
        int length=byteBuf.readInt();
        byte[] bytes=new byte[length];
        byteBuf.readBytes(bytes);
        Object deserializer=serializer.deserialize(bytes,messageType);
        out.add(deserializer);
    }
}
