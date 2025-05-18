package part1.server.services.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import part1.server.netty.nettyInitializer.NettyServerInitializer;
import part1.server.provider.ServiceProvider;
import part1.server.services.RpcServer;

/*
    @author 张星宇
 */
@AllArgsConstructor
public class NettyRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
        System.out.printf("netty服务器准备启动");
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class).
                childHandler(new NettyServerInitializer(serviceProvider));
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("服务端启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
