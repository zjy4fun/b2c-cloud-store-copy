package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.UserMapper;
import org.example.param.UserCheckParam;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public R check(UserCheckParam userCheckParam) {
        //1. 参数封装
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userCheckParam.getUserName());
        //2. 数据库查询
        Long total = userMapper.selectCount(queryWrapper);
        //3. 查询结果处理
        if (total == 0) {
            //数据库中不存在
            log.info("UserServiceImpl.check 业务结束，结果:{}", "账号可以使用");
            return R.ok("账号不存在，可以使用！");
        }
        log.info("UserServiceImpl.check 业务结束，结果:{}", "账号不可使用");
        return R.fail("账号已经存在，不可注册！");
    }
}
