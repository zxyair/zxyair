package part1.client.rpcClient;

import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

/*
    @author 张星宇
 */
public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
