package com.pit.im.webserver.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.pit.im.utils.BCryptUtil;
import com.pit.im.utils.BaseController;
import com.pit.im.webserver.entity.SysUserEntity;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author deng
 * @date 2020/1/7 9:08
 */
@RestController
@RequestMapping("/user")
public class SysUserServiceController extends BaseController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/add")
    public R add(SysUserEntity userEntity) {
        userEntity.setCreateBy(getUserId());
        userEntity.setCreateTime(now());
        String password = userEntity.getPassword();
        password = StringUtils.isEmpty(password) ? "123" : password;
        userEntity.setPassword(BCryptUtil.encode(password));
        userService.save(userEntity);
        return R.ok(null);
    }

    @GetMapping("/getUserExt")
    public R getUserExt(String loginName){
       UserExt userExt = userService.getUserExt(loginName);
       return R.ok(userExt);
    }


}
