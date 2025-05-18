package part1.client.serviceCenter;

import java.net.InetSocketAddress;

/*
    @author 张星宇
 */
public interface ServiceCenter {
    InetSocketAddress serviceDiscovery(String serviceName);
    boolean checkRetry(String serviceName);
}
