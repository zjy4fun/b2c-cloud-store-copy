package org.example.service;

import org.example.param.UserCheckParam;
import org.example.pojo.User;
import org.example.utils.R;

public interface UserService {
    /**
     * 检查账号是否可用
     * @param userCheckParam 账号参数 已经校验完毕
     * @return
     */
    R check(UserCheckParam userCheckParam);

    R register(User user);
}
