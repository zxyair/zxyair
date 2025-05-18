package part1.client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    @author 张星宇
 */

public class ServiceCache {
    //key: serviceName 服务名
    //value： addressList 服务提供者列表
    private static Map<String , List<String>> cache = new HashMap<>();
    //添加服务
    public static void addServcieToCache(String serviceName,String  address) {
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+
                    "的服务添加到本地缓存中");
        }else{
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
        }
    }
    public void replaceServiceAddress(String serviceName,String oldAddress,
                                      String newAddress){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        }else{
            System.out.println("修改失败，服务不存在");
        }
    }
    public List<String> getServiceFromCache(String serviceName){
        if(cache.containsKey(serviceName)){
            return cache.get(serviceName);
        }
        return null;
    }
    public void delete(String serviceName,String address){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.remove(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+
                    "的服务从本地缓存中删除 ");
        }else {
            System.out.println("删除失败，服务不存在");
        }
    }

    public Map getCache() {
        return cache;
    }
}
