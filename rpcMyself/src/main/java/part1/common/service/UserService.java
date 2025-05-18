package part1.common.service;

import part1.common.pojo.User;

/*
    @author 张星宇
 */
public interface UserService {
    User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
