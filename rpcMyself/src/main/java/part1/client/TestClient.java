package part1.client;

import part1.client.proxy.ClientProxy;
import part1.common.pojo.User;
import part1.common.service.UserService;

/*
    @author 张星宇
 */
public class TestClient {
    public static void main(String[] args) throws InterruptedException {
//        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9998,1);
        //引入zookeeper,动态选择ip和端口
        ClientProxy clientProxy = new ClientProxy();
        UserService userService =
                (UserService) clientProxy.getProxy(UserService.class);
        User user = userService.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + user.toString());
        Integer id =
                userService.insertUserId(User.builder().userName("张星宇").sex(true).id(1).build());
        System.out.println("向服务端插入数据成功！");


    }
}
