package com.pit.im.webserver.entity.ext;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deng
 * @date 2020/1/7 10:28
 */
@Data
public class LoginFrom implements Serializable {

    private static final long serialVersionUID = 2798807927038697612L;

    private String loginName;

    private String password;
}
