package part1.server.serviceRegister;

import java.net.InetSocketAddress;

/*
    @author 张星宇
 */
public interface ServiceRegister {
    void register(String serviceName, InetSocketAddress inetSocketAddress,
                  boolean canRetry);
}
