package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.UserMessageDao;
import com.pit.im.webserver.entity.UserMessageEntity;
import com.pit.im.webserver.service.UserMessageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;


@Service("userMessageService")
public class UserMessageServiceImpl extends ServiceImpl<UserMessageDao, UserMessageEntity> implements UserMessageService {

    @Override
    public UserMessageEntity getInfo(String userId) {
        return null;
    }

    @Override
    public List<UserMessageEntity> getList(Map<String, Object> map) {
        return null;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return 0;
    }

    @Override
    public int update(UserMessageEntity userMessage) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return 0;
    }

    @Override
    public List<UserMessageEntity> getHistoryMessageList(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<UserMessageEntity> getOfflineMessageList(Map<String, Object> map) {
        return null;
    }

    @Override
    public int getHistoryMessageCount(Map<String, Object> map) {
        return 0;
    }
}