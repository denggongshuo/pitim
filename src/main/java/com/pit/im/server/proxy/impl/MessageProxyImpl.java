package com.pit.im.server.proxy.impl;

import cn.hutool.core.date.DateUtil;
import com.pit.im.constants.ImConstants;
import com.pit.im.server.model.MessageBodyProto;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.model.MessageWrapper;
import com.pit.im.server.proxy.MessageProxy;
import com.pit.im.webserver.entity.UserMessageEntity;
import com.pit.im.webserver.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author deng
 * @date 2020/1/2 16:44
 */
@Service
@Slf4j
public class MessageProxyImpl implements MessageProxy {

    @Autowired
    private UserMessageService userMessageService;

    @Override
    public MessageWrapper convertToMessageWrapper(String sessionId, MessageProto.Model message) {
        switch (message.getCmd()) {
            case ImConstants.CmdType.BIND:
                try {
                    return new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, message.getSender(), null, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ImConstants.CmdType.HEARTBEAT:
                try {
                    return new MessageWrapper(MessageWrapper.MessageProtocol.HEART_BEAT, sessionId, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ImConstants.CmdType.ONLINE:

                break;
            case ImConstants.CmdType.OFFLINE:

                break;
            case ImConstants.CmdType.MESSAGE:
                try {
                    MessageProto.Model.Builder result = MessageProto.Model.newBuilder(message);
                    result.setTimeStamp(DateUtil.now());
                    result.setSender(sessionId);//存入发送人sessionId
                    message = MessageProto.Model.parseFrom(result.build().toByteArray());
                    //判断消息是否有接收人
                    if (StringUtils.isNotEmpty(message.getReceiver())) {

                        return new MessageWrapper(MessageWrapper.MessageProtocol.REPLY, sessionId, message.getReceiver(), message);

                    } else if (StringUtils.isNotEmpty(message.getGroupId())) {
                        return new MessageWrapper(MessageWrapper.MessageProtocol.GROUP, sessionId, null, message);
                    } else {
                        return new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, null, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }


        return null;
    }

    @Override
    public void saveOnlineMessageToDB(MessageWrapper message) {
        try {
            UserMessageEntity userMessage = convertMessageWrapperToBean(message);
            if (userMessage != null) {
                userMessage.setIsread(1);
                userMessageService.save(userMessage);
            }
        } catch (Exception e) {
            log.error("MessageProxyImpl  user " + message.getSessionId() + " send msg to " + message.getReSessionId() + " error");
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void saveOfflineMessageToDB(MessageWrapper message) {
        try{

            UserMessageEntity  userMessage = convertMessageWrapperToBean(message);
            if(userMessage!=null){
                userMessage.setIsread(0);
                userMessageService.save(userMessage);
            }
        }catch(Exception e){
            log.error("MessageProxyImpl  user "+message.getSessionId()+" send msg to "+message.getReSessionId()+" error");
            throw new RuntimeException(e.getCause());
        }
    }
    private UserMessageEntity convertMessageWrapperToBean(MessageWrapper message) {
        UserMessageEntity userMessage = null;
        try {
            MessageProto.Model msg = (MessageProto.Model) message.getBody();
            MessageBodyProto.MessageBody msgContent = MessageBodyProto.MessageBody.parseFrom(msg.getContent());
            userMessage = new UserMessageEntity();
            userMessage.setSenduser(message.getSessionId());
            userMessage.setReceiveuser(message.getReSessionId());
            userMessage.setContent(msgContent.getContent());
            userMessage.setGroupid(msg.getGroupId());
            userMessage.setCreateDate(msg.getTimeStamp());
            userMessage.setIsread(1);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        return userMessage;
    }
    @Override
    public MessageProto.Model getOnLineStateMsg(String sessionId) {
        MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
        result.setTimeStamp(DateUtil.now());
        result.setSender(sessionId);//存入发送人sessionId
        result.setCmd(ImConstants.CmdType.ONLINE);
        return result.build();
    }

    @Override
    public MessageWrapper getReConnectionStateMsg(String sessionId) {
        MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
        result.setTimeStamp(DateUtil.now());
        result.setSender(sessionId);//存入发送人sessionId
        result.setCmd(ImConstants.CmdType.RECON);
        return  new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, null,result.build());

    }

    @Override
    public MessageProto.Model getOffLineStateMsg(String sessionId) {
        MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
        result.setTimeStamp(DateUtil.now());
        result.setSender(sessionId);//存入发送人sessionId
        result.setCmd(ImConstants.CmdType.OFFLINE);
        return result.build();
    }
}
