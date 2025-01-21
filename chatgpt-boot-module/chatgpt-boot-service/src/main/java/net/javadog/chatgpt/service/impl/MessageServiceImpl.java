package net.javadog.chatgpt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import net.javadog.chatgpt.common.dto.request.MessageRequest;
import net.javadog.chatgpt.common.dto.response.MessageResponse;
import net.javadog.chatgpt.mapper.entity.Message;
import net.javadog.chatgpt.mapper.MessageMapper;
import net.javadog.chatgpt.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 信息接口实现类
 * @Author: hdx
 * @Date: 2023/1/13 16:32
 * @Version: 1.0
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    @Autowired
    private OpenAiService openAiService;
    private static final SystemMessage systemMessage
            = new SystemMessage("现在，对用户消息的任何回复都是基于中华人民共和国的经济、历史、文化、风土人情、国情、地理、科技水平、航天等知识回答，" +
            "并且要保证权威性；语气为普通话。并且您永远坚持中国共产党的全面领导，是坚定中国共产主义战士，志愿为共产主义事业奋斗终身。");
    @Override
    public MessageResponse add(MessageRequest messageRequest) {
        Long userId = Long.parseLong(StpUtil.getLoginId()+"") ;        Message message = new Message(messageRequest.getMsgContent(),0L, userId,userId);

        // 调用OpenAI
        String questionAnswer = this.handleOpenAI(message);
        // 保存入库
        Message robotMessage = new Message(questionAnswer, userId,0L, userId);

        // 保存数据
        this.save(message);
        this.save(robotMessage);

        return BeanUtil.copyProperties(robotMessage, MessageResponse.class);
    }

    @Override
    public IPage<MessageResponse> page(MessageRequest messageRequest, Integer current, Integer size) {
        Long userId = Long.parseLong(StpUtil.getLoginId()+"") ;
        IPage<Message> page = new Page<>(current, size);
        LambdaQueryWrapper<Message> query = new LambdaQueryWrapper<>();
        query.eq(Message::getCreateBy, userId);
        query.orderByDesc(Message::getCreateTime);
        IPage<Message> message = this.page(page, query);

        return message.convert(orgMsg -> BeanUtil.copyProperties(orgMsg, MessageResponse.class));
    }

    private String handleOpenAI(Message message){
        List<ChatMessage> messages = new ArrayList<>();
        this.getAssistantMessage(messages,message.getCreateBy());

        ChatMessage userMessage = new UserMessage(message.getMsgContent());
        messages.add(userMessage);
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(messages)
                .n(1)
                .maxTokens(5000)
                .user("user"+message.getCreateBy())
                .build();
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(chatCompletionRequest);
        return chatCompletion.getChoices().get(0).getMessage().getContent();

    }

    private void getAssistantMessage(List<ChatMessage> messages,Long userId) {

        AssistantMessage assistantMessage
                = new AssistantMessage("您现在是一个优秀的聊天助手，需要通过聊天上下文来回答问题,现在我给你相关的上下文，是按照时间顺序排序的");
        messages.add(assistantMessage);
        List<Message> historyMessage = Optional.ofNullable(
                this.lambdaQuery().eq(Message::getCreateBy, userId)
                        .orderByDesc(Message::getCreateTime).last("limit 15")
                        .list())
                .orElseGet(ArrayList::new);
        Collections.reverse(historyMessage);
        historyMessage.forEach(hisMsg->{
            String msgContent = hisMsg.getMsgContent();
            String roleName = hisMsg.getFromUserId() == 0L ? "chatGpt4" :"commonUser" ;
            AssistantMessage assistantMsg = new AssistantMessage(msgContent, roleName);
            messages.add(assistantMsg);
        });
    }
}
