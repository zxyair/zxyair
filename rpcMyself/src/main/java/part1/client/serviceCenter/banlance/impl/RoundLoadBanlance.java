package part1.client.serviceCenter.banlance.impl;

import part1.client.serviceCenter.banlance.LoadBanlance;

import java.util.List;

/*
    @author 张星宇
 */
public class RoundLoadBanlance implements LoadBanlance {
    private int choose = -1;
    @Override
    public String banlance(List<String> addressList) {
        choose++;
        choose = choose%addressList.size();
        System.out.println("轮询负载均衡器选择了"+choose+"服务器");
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
