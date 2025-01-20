package net.javadog.chatgpt.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户实体
 *
 * @author: hdx
 * @Date: 2023-01-10 11:43
 * @version: 1.0
 **/
@Data
@NoArgsConstructor
public class Message {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    public Message(String msgContent,Long toUserId, Long fromUserId,Long createBy ){
        this.msgContent = msgContent;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.createTime = new Date();
        this.createBy = createBy;
    }
}
