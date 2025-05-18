package part1.common.serializer;

import part1.common.serializer.impl.JsonSerializer;
import part1.common.serializer.impl.ObjectSerializer;

/*
    @author 张星宇
 */
public interface Serializer {
    byte[] serialize(Object object);
    Object deserialize(byte[] bytes, int MessageType);
    int getType();
    static Serializer getSerializerByCode(int code)
    {
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
