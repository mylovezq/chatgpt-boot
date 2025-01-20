package net.javadog.chatgpt.controller.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import net.javadog.chatgpt.common.response.ResponseData;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (httpServletRequest.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())){
            return true;
        }
        // 实现权限校验逻辑
        String authHeader = httpServletRequest.getHeader("Access-Token");
        if (StrUtil.isBlank(authHeader) || !StpUtil.isLogin()) {
            ResponseData error
                    = ResponseData.error(HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
            httpServletResponse.setStatus(200);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(error.toString());
            return false;
        }
        return true;
    }


}
