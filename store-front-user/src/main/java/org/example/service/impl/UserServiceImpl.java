package org.example.service.impl;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.UserConstants;
import org.example.mapper.UserMapper;
import org.example.param.UserCheckParam;
import org.example.param.UserLoginParam;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.MD5Util;
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

    /**
     * 注册业务
     * 1. 检查账号是否存在
     * 2. 密码加密处理
     * 3. 插入数据库数据
     * 4. 返回结果封装
     * @param user 参数已经校验，但是密码是明文！
     * @return 结果 001， 004
     */
    @Override
    public R register(User user) {
        // 1. 检查账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        Long total = userMapper.selectCount(queryWrapper);

        if (total > 0) {
            log.info("UserServiceImpl.register 业务结束，结果: {}", "账号存在，注册失败");
            return R.fail("账号已经存在，不可注册！");
        }

        // 2. 密码加密处理，注意要加盐
        /**
         * MD5 是一种不可逆转加密方式，只能加密不能解密
         *     固定的明文加密以后的密文是固定的
         * MD5 可以暴力破解
         *     穷举法
         *     简单的字符串都是不安全的
         *     提示用户密码复杂度
         *     加盐处理  用户的密码 1 + 字符串[盐]9999 = 10000
         */

        String newPwd = MD5Util.encode(user.getPassword() + UserConstants.USER_SALT);
        user.setPassword(newPwd);

        // 3. 插入数据库数据
        int rows = userMapper.insert(user);
        // 4. 返回封装结果
        if (rows == 0) {
            log.info("UserServiceImpl.register 业务结束，结果:{}", "数据插入失败！注册失败！");
            return R.fail("注册失败！请稍后再试");
        }
        log.info("UserServiceImpl.register 业务结束，结果: {}", "注册成功!");

        return R.ok("注册成功！");
    }

    /**
     * 登录业务：
     * 1. 密码的加密和加盐处理
     * 2. 账号和密码进行数据库查询，返回一个完整的数据库 user 对象
     * 3. 判断返回结果
     * @param userLoginParam  账号和密码 已经校验 但是密码是明文
     * @return 结果 001  004
     */
    @Override
    public R login(UserLoginParam userLoginParam) {
        //1. 密码处理
        String newPwd = MD5Util.encode(userLoginParam.getPassword() + UserConstants.USER_SALT);

        //2. 数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userLoginParam.getUserName());
        queryWrapper.eq("password", newPwd);

        User user = userMapper.selectOne(queryWrapper);
        //3. 结果处理
        if (user == null) {
            log.info("UserServiceImpl.login 业务结束，结果:{}", "账号和密码错误!");
            return R.fail("账号或者密码错误!");
        }
        log.info("UserServiceImpl.login 业务结束，结果:{}", "登录成功!");
        // 不返回 password !!!
        user.setPassword(null);
        return R.ok("登录成功！", user);
    }
}
