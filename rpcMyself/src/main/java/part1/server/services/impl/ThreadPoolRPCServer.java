package part1.server.services.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import part1.server.services.RpcServer;
import part1.server.provider.ServiceProvider;
import part1.server.services.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
    @author 张星宇
 */
@AllArgsConstructor
@Data
public class ThreadPoolRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    private final ThreadPoolExecutor threadPool;
    public ThreadPoolRPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        this.threadPool = new ThreadPoolExecutor(2, 10,
                60, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(50));
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功！");
            while (true) {
                // 阻塞等待客户端连接
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket, serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
