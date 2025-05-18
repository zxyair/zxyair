package part1.client.retry;

import com.github.rholder.retry.*;
import part1.client.rpcClient.RpcClient;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
    @author 张星宇
 */
public class guavaRetry {
    private RpcClient client;
    public RpcResponse senServiceWithRetry(RpcRequest request,RpcClient client){
        this.client=client;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfException()
                .retryIfResult(response -> Objects.equals(response.getCode(),
                        500))
                .withWaitStrategy(WaitStrategies.fixedWait(2,
                        TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("重试次数："+attempt.getAttemptNumber());
                    }
                        })
                .build();
        try {
            return retryer.call(()->client.sendRequest(request));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        }
        return RpcResponse.fail("重试失败");
    }
}
