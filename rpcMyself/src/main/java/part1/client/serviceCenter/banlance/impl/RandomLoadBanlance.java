package part1.client.serviceCenter.banlance.impl;

import part1.client.serviceCenter.banlance.LoadBanlance;

import java.util.List;
import java.util.Random;

/*
    @author 张星宇
 */
public class RandomLoadBanlance implements LoadBanlance {
    @Override
    public String banlance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("随机负载均衡器选择了"+choose+"服务器");
        return null;
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
