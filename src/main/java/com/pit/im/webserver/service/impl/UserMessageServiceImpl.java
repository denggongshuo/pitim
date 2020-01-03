package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.entity.UserMessageEntity;
import com.pit.im.webserver.service.UserMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author deng
 * @date 2020/1/2 16:47
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {
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
    public void save(UserMessageEntity userMessage) {

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
