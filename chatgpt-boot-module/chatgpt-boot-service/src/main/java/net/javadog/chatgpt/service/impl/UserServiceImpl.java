package net.javadog.chatgpt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.javadog.chatgpt.common.enums.ServiceErrorEnum;
import net.javadog.chatgpt.common.exception.ServiceException;
import net.javadog.chatgpt.common.dto.request.LoginRequest;
import net.javadog.chatgpt.common.dto.request.RegisterRequest;
import net.javadog.chatgpt.common.dto.response.UserResponse;
import net.javadog.chatgpt.mapper.entity.Message;
import net.javadog.chatgpt.mapper.entity.User;
import net.javadog.chatgpt.mapper.UserMapper;
import net.javadog.chatgpt.service.MessageService;
import net.javadog.chatgpt.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户接口实现类
 *
 * @author: hdx
 * @Date: 2023-01-10 11:55
 * @version: 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private MessageService messageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(RegisterRequest registerRequest) {
        // 校验是否已存在此手机号
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerRequest.getUsername());
        User one = this.getOne(queryWrapper);
        // 判断用户是否存在
        if (null != one) {
            // 已存在用户,抛出异常
            throw new ServiceException(ServiceErrorEnum.USER_IS_EXIT);
        }
        User user = new User();
        BeanUtil.copyProperties(registerRequest, user);
        // 密码加密
        String password = registerRequest.getPassword();
        user.setPassword(SecureUtil.md5(password));
        user.setCreateTime(new Date());
        boolean saveFlag = this.save(user);
        // 补充chatGPT第一句
        Message msg = new Message();
        msg.setFromUserId(0L);
        msg.setMsgContent("Jiushi欢迎您到来，快来和chatGPT4聊聊吧！\n" +
                "猜您喜欢：<br/> \n" +
                "1.<a href=\"https://github.com/mylovezq/chatgpt-boot\">【CHATGPT】手摸手，带你玩转CHATGPT4</a> <br/> \n");
        msg.setToUserId(user.getId());
        msg.setCreateBy(one.getId());
        msg.setCreateTime(new Date());
        messageService.save(msg);
        return saveFlag;
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                .eq(User::getPassword, SecureUtil.md5(password));
        User user = this.getOne(queryWrapper);
        if(ObjectUtil.isNull(user)){
            throw new ServiceException(ServiceErrorEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        UserResponse userResponse = new UserResponse();
        BeanUtil.copyProperties(user, userResponse);
        StpUtil.login(user.getId());
        userResponse.setAccessToken(StpUtil.getTokenValue());
        return userResponse;
    }

}
