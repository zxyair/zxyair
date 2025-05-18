package part1.server.services.impl;

import lombok.AllArgsConstructor;
import part1.server.services.RpcServer;
import part1.server.provider.ServiceProvider;
import part1.server.services.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
    @author 张星宇
 */
@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功！");
            while (true) {
                // 阻塞等待客户端连接
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
