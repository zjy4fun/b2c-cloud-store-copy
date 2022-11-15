package org.example.controller;

import org.example.param.AddressListParam;
import org.example.param.AddressRemoveParam;
import org.example.pojo.Address;
import org.example.service.AddressService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/address")
public class FrontAddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    public R list(@RequestBody @Validated AddressListParam addressListParam, BindingResult result) {
        if (result.hasErrors()) {
            return R.fail("参数异常，查询失败！");
        }
        return addressService.list(addressListParam.getUserId());
    }

    @PostMapping("save")
    public R save(@RequestBody @Validated Address address, BindingResult result){ //直接复用 Address 实体类，json传参的时候应该注意字段名需要保持一致
        if (result.hasErrors()) {
            return R.fail("参数异常，保存失败！");
        }
        return addressService.save(address);
    }

    @PostMapping("remove")
    public R remove(@RequestBody @Validated AddressRemoveParam addressRemoveParam, BindingResult result){
        if (result.hasErrors()) {
            return R.fail("参数异常，删除失败");
        }
        return addressService.remove(addressRemoveParam.getId());
    }
}
