package part1.client.serviceCenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part1.client.cache.ServiceCache;
import part1.client.serviceCenter.ZkWatcher.watchZK;
import part1.client.serviceCenter.banlance.impl.ConsistencyHashBanlance;

import java.net.InetSocketAddress;
import java.util.List;

/*
    @author 张星宇
 */
public class ZKServiceCenter implements ServiceCenter{
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETYR = "CanRetry";
    private ServiceCache cache;

    //负责zookeeper客户端的初始化，并与zookeeper服务端进行连接
    public ZKServiceCenter() throws InterruptedException {
        RetryPolicy policy =new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别
        // 为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181").sessionTimeoutMs(40000)
                .retryPolicy(policy).namespace(ROOT_PATH)
                .build();
         this.client.start();
         this.cache = new ServiceCache();
        watchZK watcher = new watchZK(client, cache);
        watcher.watchToUpdate(ROOT_PATH);
        System.out.println("zk连接成功");
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先从本地缓存找
            List<String> serviceAddress = cache.getServiceFromCache(serviceName);
            System.out.println("serviceAddress"+serviceAddress);
            System.out.println("cache"+cache.getCache().toString());
            if(serviceAddress==null){
                System.out.println("缓存未找到");
                serviceAddress = client.getChildren().forPath("/" + serviceName);
                System.out.println("serviceName"+serviceName);
                System.out.println("serviceAddress"+serviceAddress);
            }
            // 这里默认用的第一个，后面加负载均衡
            String string =
                    new ConsistencyHashBanlance().banlance(serviceAddress);
            return parseAddress(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public boolean checkRetry(String serviceName) {
        boolean canRetry = false;
        try {
            List<String> serviceList =
                    client.getChildren().forPath("/" + RETYR);
            for(String s : serviceList){
                if(s.equals(serviceName)){
                    System.out.println("服务"+serviceName+"可以重试");
                    canRetry = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  canRetry;
    }

    private InetSocketAddress parseAddress(String string) {
        String[] split = string.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
