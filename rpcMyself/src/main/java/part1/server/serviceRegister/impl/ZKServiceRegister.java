package part1.server.serviceRegister.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import part1.server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;

/*
    @author 张星宇
 */
public class ZKServiceRegister implements ServiceRegister {
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";

    public  ZKServiceRegister() {
        RetryPolicy policy =
                new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181").sessionTimeoutMs(40000)
                .retryPolicy(policy).namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zk连接成功");
    }

    @Override
    public void register(String serviceName,
                         InetSocketAddress inetSocketAddress,boolean canRetry) {
        //调试代码
//        if (client == null) {
//            throw new IllegalStateException("CuratorFramework 未初始化");
//        }
//        try {
//        String servicePath = "/" + serviceName;
//        System.out.println("准备创建持久节点: " + servicePath);
//        System.out.println("client: " + client);
//
//        if (client.checkExists().forPath(servicePath) == null) {
//            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
//                    .forPath(servicePath);
//            System.out.println("创建持久节点: " + servicePath);
//        } else {
//            System.out.println("服务路径已存在: " + servicePath);
//        }
//
//        String path = ROOT_PATH + "/" + serviceName + "/" + getServiceAddress(inetSocketAddress);
//        System.out.println("准备创建临时节点: " + path);
//
//        // 确保父节点是 PERSISTENT 类型
//        String parentPath = ROOT_PATH + "/" + serviceName;
//        if (client.checkExists().forPath(parentPath) == null) {
//            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
//                    .forPath(parentPath);
//            System.out.println("创建持久父节点: " + parentPath);
//        } else {
//            System.out.println("父节点已存在: " + parentPath);
//        }
//
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
//        System.out.println("创建临时节点: " + path);
//    } catch (org.apache.zookeeper.KeeperException e) {
//        System.out.println("KeeperException: " + e.getMessage());
//        e.printStackTrace();
//        System.out.println("注册服务失败");
//    } catch (InterruptedException e) {
//        System.out.println("InterruptedException: " + e.getMessage());
//        e.printStackTrace();
//        System.out.println("注册服务失败");
//    } catch (Exception e) {
//        System.out.println("其他异常: " + e.getMessage());
//        e.printStackTrace();
//        System.out.println("注册服务失败");
//    }

        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
//            System.out.println("/" + serviceName);
            if(client.checkExists().forPath("/" + serviceName) == null){
                System.out.println(serviceName+"为null");
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            // 路径地址，一个/代表一个节点
            String path = "/" + serviceName +"/"+ getServiceAddress(inetSocketAddress);
            System.out.println("/" + serviceName +"/"+ getServiceAddress(inetSocketAddress));
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            if(canRetry){
                path = "/"+RETRY+ "/"+serviceName ;
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                System.out.println("接入可重试节点"+path);

            }
        } catch (Exception e) {
            System.out.println("此服务已存在");
            e.printStackTrace();
        }
/*        try {
            String serviceAddress = getServiceAddress(inetSocketAddress);
            String path = "/" + serviceName + "/" + serviceAddress;
            System.out.println(path);

            // 检查并创建父路径
            String parentPath = path.substring(0, path.lastIndexOf('/'));
            if (client.checkExists().forPath(parentPath) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(parentPath);
            }

            // 创建临时节点
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.UnimplementedException e) {
            System.out.println("ZooKeeper 不支持该操作，请检查版本兼容性或配置");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("注册服务时发生错误");
            e.printStackTrace();
        }*/


    }
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }
    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
