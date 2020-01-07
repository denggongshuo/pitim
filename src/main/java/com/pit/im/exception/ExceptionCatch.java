package com.pit.im.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 统一异常捕获类
 *
 * @author Administrator
 * @version 1.0
 * @create 2018-09-14 17:32
 **/
@ControllerAdvice//控制器增强
@Slf4j
public class ExceptionCatch {

    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, IErrorCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, IErrorCode> builder = ImmutableMap.builder();


    //捕获RRException此类异常
    @ExceptionHandler(REException.class)
    @ResponseBody
    public R handleRRException(REException rRException) {
        IErrorCode iErrorCode = rRException.getIErrorCode();
        R r = new R(iErrorCode);
        //记录日志
        log.error("catch exception:{}", rRException.getMessage());

        return r;
    }

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R exception(Exception exception) {
        exception.printStackTrace();
        //记录日志
        log.error("catch exception:{}", exception.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        IErrorCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new R(resultCode);
        } else {
            //返回99999异常
            return new R(ErrorCode.SYS_ERROR);
        }


    }

    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class, ErrorCode.INVALID_PARAM);
    }
}
