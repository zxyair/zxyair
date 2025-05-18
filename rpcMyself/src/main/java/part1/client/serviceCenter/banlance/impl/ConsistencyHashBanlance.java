package part1.client.serviceCenter.banlance.impl;

import part1.client.serviceCenter.banlance.LoadBanlance;

import java.util.*;

/*
    @author 张星宇
 */
public class ConsistencyHashBanlance implements LoadBanlance {
    // 虚拟节点的个数
    private static final int VIRTUAL_NUM = 5;

    // 虚拟节点分配，key是hash值，value是虚拟节点服务器名称
    private static SortedMap<Integer, String> shards = new TreeMap<Integer, String>();

    // 真实节点列表
    private static List<String> realNodes = new LinkedList<String>();

    //模拟初始服务器
    private static String[] servers =null;

    private static void init(List<String> serverList){
        for(String server : serverList){
            realNodes.add(server);
            System.out.println("真实节点[" + server + "] 被添加");
            for(int i=0;i<VIRTUAL_NUM;i++){
                String virtualNode = server+"&&VN"+i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }
    public static String getServer(String node , List<String> serverList) {
        init(serverList);
        int hash = getHash(node);
        Integer key = null;
        SortedMap<Integer, String> subMap = shards.tailMap(hash);
        if (subMap.isEmpty()) {
            key = shards.lastKey();
        } else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        System.out.println(virtualNode);
        return virtualNode.substring(0,virtualNode.indexOf("&&"));
    }
    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("真实节点[" + node + "] 被添加");
            for(int i=0;i<VIRTUAL_NUM;i++){
                String virtualNode = node+"&&VN"+i;
                int hash = getHash(virtualNode);
                shards.put(hash,node);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }
    //FVN——32位的FNV哈希算法
    private static int getHash(String virtualNode) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < virtualNode.length(); i++)
            hash = (hash ^ virtualNode.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;

    }

    @Override
    public String banlance(List<String> addressList) {
        String random = UUID.randomUUID().toString().toString();
        return getServer(random,addressList);
    }



    @Override
    public void delNode(String node) {
        if(realNodes.contains(node)){
            realNodes.remove(node);
            System.out.println("真实节点[" + node + "] 被删除");
            for(int i=0;i<VIRTUAL_NUM;i++){
                String virtualNode = node+"&&VN"+i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被删除");
            }
        }
    }
}
