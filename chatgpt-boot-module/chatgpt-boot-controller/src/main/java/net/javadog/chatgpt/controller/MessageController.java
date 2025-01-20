package net.javadog.chatgpt.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.extern.slf4j.Slf4j;
import net.javadog.chatgpt.common.response.ResponseData;
import net.javadog.chatgpt.common.dto.request.MessageRequest;
import net.javadog.chatgpt.common.dto.response.MessageResponse;
import net.javadog.chatgpt.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 消息控制器
 * @Author: hdx
 * @Date: 2023/2/16 17:37
 * @Version: 1.0
 */
@RestController
@Slf4j

@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;
    @PostMapping
    public ResponseData send(@RequestBody MessageRequest messageRequest) {
        log.info("消息数据,messageRequest = {}", JSONUtil.toJsonStr(messageRequest));
        MessageResponse messageResponse = messageService.add(messageRequest);
        return ResponseData.success(messageResponse);
    }

    @GetMapping
    public ResponseData get(final MessageRequest messageRequest,
                            final @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            final @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        IPage<MessageResponse> page = messageService.page(messageRequest, current, size);
        return ResponseData.success(page);
    }
}
