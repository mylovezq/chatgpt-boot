package net.javadog.chatgpt.controller;


import net.javadog.chatgpt.common.enums.ServiceErrorEnum;
import net.javadog.chatgpt.common.exception.ServiceException;
import net.javadog.chatgpt.common.response.ResponseData;
import net.javadog.chatgpt.common.dto.request.RegisterRequest;
import net.javadog.chatgpt.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: 注册控制器
 * @Author: hdx
 * @Date: 2022/1/29 14:57
 * @Version: 1.0
 */

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    private UserService userService;

    @PostMapping
    public ResponseData register(@RequestBody @Valid RegisterRequest registerRequest) {
        // 校验两次密码是否一致
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordVerify())) {
            throw new ServiceException(ServiceErrorEnum.CONFIRMATION_PASSWORD_ERROR);
        }
        userService.register(registerRequest);
        return ResponseData.success();
    }
}
