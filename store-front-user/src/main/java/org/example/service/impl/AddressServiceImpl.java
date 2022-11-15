package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.AddressMapper;
import org.example.pojo.Address;
import org.example.service.AddressService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    /**
     * 根据用户 id 查询地址数据
     *   1. 直接进行数据库查询
     *   2. 结果封装即可
     * @param userId
     * @return
     */
    @Override
    public R list(Integer userId) {
        // 1. 封装查询参数
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Address> addressList = addressMapper.selectList(queryWrapper);

        //2. 结果封装
        R ok = R.ok("查询成功", addressList);
        log.info("AddressServiceImpl.list 业务结束，结果:{}", ok);
        return ok;
    }

    /**
     * 插入地址数据，插入成功以后，返回新的数据集合
     * @param address 地址数据已经校验完毕
     * @return 返回数据即可
     */
    @Override
    public R save(Address address) {
        //插入数据
        int rows = addressMapper.insert(address);
        if (rows  == 0) {
            log.info("AddressServiceImpl.save业务结束，结果:{}", "插入地址数据失败");
            return R.fail("插入地址数据失败");
        }
        //复用查询业务
        return list(address.getUserId());
    }

    @Override
    public R remove(Integer addressId) {
        int rows = addressMapper.deleteById(addressId);
        if (rows == 0) {
            log.info("AddressServiceImpl.remove业务结束，结果:{}", "删除地址数据失败");
            return R.fail("删除地址数据失败");
        }
        return R.ok("删除地址数据成功");
    }
}
