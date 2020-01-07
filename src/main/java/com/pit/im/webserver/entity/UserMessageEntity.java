package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author deng
 * @date 2020/1/2 16:49
 */
@Data
@TableName("user_message")
public class UserMessageEntity implements Serializable {

    private static final long serialVersionUID = 6129241549286284033L;

    //发送人
    private String senduser;
    //发送人昵称或姓名
    private String sendusername;
    //发送人头像
    private String avatar;
    //接收人
    private String receiveuser;
    //群ID
    private String groupid;
    //是否已读 0 未读  1 已读
    private Integer isread;
    //类型 0 单聊消息  1 群消息
    private Integer type = 0;
    //消息内容
    private String content;

    private String createDate = "";


    /**
     * 设置：群ID
     */
    public void setGroupid(String groupid) {
        if (StringUtils.isNotEmpty(groupid)) {
            setType(1);
        }
        this.groupid = groupid;
    }
}
