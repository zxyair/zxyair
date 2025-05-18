package part1.server;

import part1.common.service.UserServiceImpl;
import part1.server.provider.ServiceProvider;
import part1.server.services.RpcServer;
import part1.server.services.impl.NettyRPCServer;

/*
    @author 张星宇
 */
public class TestServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",9999);
        serviceProvider.providerServiceProvider(userService,true);
        // 简单实现——单线程
//        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
        //线程池实现
//        ThreadPoolRPCServer threadPoolRPCServer =
//                new ThreadPoolRPCServer(serviceProvider);
//        threadPoolRPCServer.start(8899);
        RpcServer rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(9999);
    }

}
