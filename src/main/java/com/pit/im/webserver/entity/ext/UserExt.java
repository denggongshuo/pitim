package com.pit.im.webserver.entity.ext;

import com.pit.im.webserver.entity.SysUserEntity;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author deng
 * @date 2020/1/7 9:24
 */
@Data
public class UserExt extends SysUserEntity {

    //权限信息
    private Set<String> permissions;

}
