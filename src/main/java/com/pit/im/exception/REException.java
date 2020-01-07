package com.pit.im.exception;


import com.baomidou.mybatisplus.extension.api.IErrorCode;

/**
 * 自定义异常类型
 *
 * @author Administrator
 * @version 1.0
 * @create 2018-09-14 17:28
 **/
public class REException extends RuntimeException {

    //错误代码
    IErrorCode iErrorCode;

    public REException(IErrorCode iErrorCode) {
        this.iErrorCode = iErrorCode;
    }

    public IErrorCode getIErrorCode() {
        return iErrorCode;
    }



}
