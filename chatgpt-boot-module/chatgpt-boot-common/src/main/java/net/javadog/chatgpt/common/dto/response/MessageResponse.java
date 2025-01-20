package net.javadog.chatgpt.common.dto.response;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class MessageResponse {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息发送方
     */
    private Long fromUserId;

    /**
     * 消息接收方
     */
    private Long toUserId;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    public MessageResponse(String msgContent, Long toUserId ) {
        this.msgContent = msgContent;
        this.createTime = new Date();
        this.createBy = 0L;
        this.fromUserId = 0L;
        this.toUserId = toUserId;
        this.id = IdWorker.getId();

    }

    public MessageResponse(){

    }

}
