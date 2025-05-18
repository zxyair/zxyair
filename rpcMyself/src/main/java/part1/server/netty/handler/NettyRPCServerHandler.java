package part1.server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;
import part1.server.provider.ServiceProvider;
import part1.server.ratelimit.RateLimit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
    @author 张星宇
 */
@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider serviceProvide;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcRequest rpcRequest) throws Exception {
        RpcResponse response=getResponse(rpcRequest);
        System.out.println("接收到请求，返回响应");
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName=rpcRequest.getInterfaceName();
        RateLimit rataLimit = serviceProvide.rateLimitProvider.getRateLimit(interfaceName);
        if(!rataLimit.getToken()){
            return RpcResponse.fail("请求太快，请稍后再试");
        }
        //得到服务端相应服务实现类
        Object service = serviceProvide.getServiceProvider(interfaceName);
        //反射调用方法
        Method method=null;
        try {
            method= service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
            Object invoke=method.invoke(service,rpcRequest.getParams());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail("方法执行错误");
        }
    }

}
