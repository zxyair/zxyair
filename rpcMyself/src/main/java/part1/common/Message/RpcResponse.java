package part1.common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    @author 张星宇
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    //响应中包含响应码、相应信息、具体数据
    private int code;
    private String message;
    private Object data;
    private Class<?> dataType;
    public static RpcResponse success(Object data){
        return RpcResponse.builder().code(200).dataType(data.getClass()).data(data).build();
    }
    public static RpcResponse fail(String message){
        return RpcResponse.builder().code(500).message("服务器错误").build();
    }
    //更新：加入传输数据的类型，以便在自定义序列化器中解析


}
