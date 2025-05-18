package part1.server.services.work;

import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;
import part1.server.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/*
    @author 张星宇
 */
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;
    public WorkThread(Socket socket, ServiceProvider serviceProvider) {
        this.socket = socket;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());
            RpcRequest request = (RpcRequest) objectInputStream.readObject();
            RpcResponse response = getResponse(request);
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private RpcResponse getResponse(RpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Object serviceProvider = this.serviceProvider.getServiceProvider(interfaceName);
        Method method = null;
        try {
            method = serviceProvider.getClass().getMethod(request.getMethodName(), request.getParamsType());
            Object invoke = method.invoke(serviceProvider, request.getParams());
            return RpcResponse.success(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            return RpcResponse.fail("方法执行失败");
        }
    }
}
