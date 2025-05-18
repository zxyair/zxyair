package part1.client.proxy;

import part1.client.circuitBreaker.CircuitBreaker;
import part1.client.circuitBreaker.CircuitBreakerProvider;
import part1.client.retry.guavaRetry;
import part1.client.rpcClient.RpcClient;
import part1.client.rpcClient.impl.NettyRpcClient;
import part1.client.rpcClient.impl.SimpleSocketRpcClient;
import part1.client.serviceCenter.ServiceCenter;
import part1.client.serviceCenter.ZKServiceCenter;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
    @author 张星宇
 */

public class ClientProxy implements InvocationHandler {
/*基于socket io通信
    private String host;
    private int port;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName()).
                methodName(method.getName()).
                params(args).paramsType(method.getParameterTypes()).build();
        RpcResponse response = IOClient.sendRequest(host, port, request);
        return response.getData();
    }
*/
    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;

    public ClientProxy() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenter();
        this.rpcClient = new NettyRpcClient(serviceCenter);
        circuitBreakerProvider = new CircuitBreakerProvider();
    }

    public ClientProxy(String host, int port, int choose) throws InterruptedException {
        switch (choose){
            case 0:
                rpcClient = new SimpleSocketRpcClient(host,port);
                break;
            case 1:
                rpcClient = new NettyRpcClient(new ZKServiceCenter());
                break;
        }
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName()).
                        methodName(method.getName()).
                        params(args).paramsType(method.getParameterTypes()).build();
        System.out.println("request准备发送:"+request);

        //熔断判断
        CircuitBreaker circuitBreaker =
                circuitBreakerProvider.getCircuitBreaker(request.getInterfaceName());
        if(!circuitBreaker.allowRequest()){
            return  null;
        }

        RpcResponse response = null;
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            response = new guavaRetry().senServiceWithRetry(request,rpcClient);
        }else{
            response = rpcClient.sendRequest(request);
        }
        if(response.getCode()==200){
            circuitBreaker.recordSuccess();
        }
        if(response.getCode()==500){
            circuitBreaker.recordFail();
        }
        System.out.println("response接收到:"+response);
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this);
        return (T) o;
    }
}
