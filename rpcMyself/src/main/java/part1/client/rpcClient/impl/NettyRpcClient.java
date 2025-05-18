package part1.client.rpcClient.impl;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part1.client.netty.nettyInitializer.NettyClientInitializer;
import part1.client.rpcClient.RpcClient;
import part1.client.serviceCenter.ServiceCenter;
import part1.client.serviceCenter.ZKServiceCenter;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.net.InetSocketAddress;


/*
    @author 张星宇
 */
public class NettyRpcClient implements RpcClient {
    private static  final Bootstrap bootstrap = new Bootstrap();
    private static final EventLoopGroup group = new NioEventLoopGroup();
    private ServiceCenter serviceCenrer;

    public NettyRpcClient(ServiceCenter serviceCenter) throws InterruptedException {
        this.serviceCenrer = new ZKServiceCenter();
    }

    static {
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }

    public RpcResponse sendRequest(RpcRequest request) {
        InetSocketAddress address = serviceCenrer.serviceDiscovery(request.getInterfaceName());
        //
        String host = address.getHostName();
        System.out.println("获取到地址"+host);
        int port = address.getPort();
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request);
            // 等待关闭
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();
            System.out.println(response);
            return  response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  null;
        }
    }
}


