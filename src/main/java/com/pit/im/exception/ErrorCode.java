package com.pit.im.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;


public enum ErrorCode implements IErrorCode {
    INVALID_PARAM(100, "非法参数"),
    ACCOUNT_NOTEXITS(102, "用户不存在"),
    PASSWORD_ERROR(103, "密码错误"),
    USER_FORBID(103, "用户被封"),
    APPLY_TOKEN_FAIL(104, "申请令牌失败！"),
    LOGIN_FAIL(401, "登录失败"),
    NOT_AUTH(403, "权限不足"),
    SYS_ERROR(500L, "服务器出错");

    private final long code;
    private final String msg;

    private ErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public long getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", this.code, this.msg);
    }
}