package com.pit.im.auth.filter.auth;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author deng
 * @date 2020/1/8 16:32
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+49)
@WebFilter(filterName = "AuthFilter",urlPatterns = "/**")
public class AuthFilter implements Filter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String accessToken = "access_token";

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String key = null;
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
            key = accessToken + ":" + request.getHeader(accessToken);
            if (StringUtils.isEmpty(key)) {
                key = accessToken + ":" + request.getParameter(accessToken);
            }

            String jwt = redisTemplate.opsForValue().get(key);
            Map<String,Object> jsonObj = (Map) JSON.parse(jwt);
            String jwt_token = jsonObj.get("jwt_token").toString();
            requestWrapper.addHeader("Authorization", "Bearer "+jwt_token);


            chain.doFilter(requestWrapper, res);

        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(req, res);
    }


    public void init(FilterConfig config) throws ServletException {

    }


}
