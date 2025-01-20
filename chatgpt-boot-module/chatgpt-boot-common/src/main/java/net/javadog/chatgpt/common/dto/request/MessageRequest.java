package net.javadog.chatgpt.common.dto.request;

import lombok.Data;

import java.util.Date;

/**
 * 消息Response
 *
 * @author: hdx
 * @Date: 2023-01-10 11:43
 * @version: 1.0
 **/
@Data
public class MessageRequest {

    /**
     * 消息内容
     */
    private String msgContent;



}
