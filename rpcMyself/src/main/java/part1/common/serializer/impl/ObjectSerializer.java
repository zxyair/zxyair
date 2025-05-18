package part1.common.serializer.impl;

import part1.common.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
    @author 张星宇
 */
public class ObjectSerializer implements Serializer {
    //实现java自带的序列化器，//利用Java io 对象 -》字节数组
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int MessageType) {
        Object object = null;
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public int getType() {
        return 0;
    }
}
