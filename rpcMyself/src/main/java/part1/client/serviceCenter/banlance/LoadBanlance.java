package part1.client.serviceCenter.banlance;

import java.util.List;

/*
    @author 张星宇
 */
public interface LoadBanlance {
    String banlance(List<String> addressList);
    void addNode(String node);
    void delNode(String node);
}
