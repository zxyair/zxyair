package part1.common.serializer.impl;

        import com.alibaba.fastjson.JSON;
        import com.alibaba.fastjson.JSONObject;
        import part1.common.Message.RpcRequest;
        import part1.common.Message.RpcResponse;
        import part1.common.pojo.User;
        import part1.common.serializer.Serializer;

/*
    @author 张星宇
 */
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = JSONObject.toJSONBytes(object);
        return  bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                Object[] objects = new Object[request.getParams().length];
                for (int i = 0; i < request.getParams().length; i++) {
                    Class<?> paramType = request.getParamsType()[i];
                    if(!paramType.isAssignableFrom(request.getParams()[i].getClass())){
                        objects[i] = JSON.parseObject(request.getParams()[i].toString(),
                                request.getParamsType()[i]);
                    }else objects[i]  = request.getParams()[i];
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1 :
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getDataType();
                System.out.println("response解码开始");
                System.out.println("dataType:"+dataType);
                dataType = dataType == null ? User.class : dataType;
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(),
                            dataType));
                }
/*                if (!dataType.isAssignableFrom(response.getData().getClass())) {
                    if (response.getData() instanceof JSONObject) {
                        response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), dataType));
                    } else {
                        // 处理其他类型的情况
                        throw new ClassCastException("无法将 " + response.getData().getClass().getName() + " 转换为 " + dataType.getName());
                    }
                }*/
                System.out.println("response解码完成");
                obj = response;
                break;
            default:
                throw new IllegalArgumentException("不识别的数据包：" + messageType);
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
