package part1.server.provider;

import part1.server.ratelimit.provider.RateLimitProvider;
import part1.server.serviceRegister.ServiceRegister;
import part1.server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/*
    @author 张星宇
 */
public class ServiceProvider {
    public RateLimitProvider rateLimitProvider;
    private Map<String ,Object> interfaceProvider;
    private int port;
    public String host;
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host,int port) {
        this.interfaceProvider = new HashMap<>();
        this.port = port;
        this.host = host;
        this.serviceRegister = new ZKServiceRegister();
        rateLimitProvider = new RateLimitProvider();
    }

    public void providerServiceProvider(Object service,boolean canRetry){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceClass = service.getClass().getInterfaces();
        for (Class<?> clazz : interfaceClass) {
            interfaceProvider.put(clazz.getName(),service);
            System.out.println(clazz.getName()+host+port);
            serviceRegister.register(clazz.getName(),
                    new InetSocketAddress(host,port),canRetry);
            System.out.println(""+clazz.getName()+" 注册成功");
        }
    }
    public Object getServiceProvider(String serviceName){
        return interfaceProvider.get(serviceName);
    }
}
