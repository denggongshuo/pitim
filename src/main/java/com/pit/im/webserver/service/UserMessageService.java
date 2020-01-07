package com.pit.im.webserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.im.webserver.entity.UserMessageEntity;

import java.util.List;
import java.util.Map;

/**
 * @author deng
 * @date 2020/1/2 16:47
 */
public interface UserMessageService extends IService<UserMessageEntity> {

    UserMessageEntity getInfo(String userId);

    List<UserMessageEntity> getList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    boolean save(UserMessageEntity userMessage);

    int update(UserMessageEntity userMessage);

    int delete(Long id);

    int deleteBatch(Long[] ids);

    /**
     * 获取历史记录
     * @param map
     * @return
     */
    List<UserMessageEntity> getHistoryMessageList(Map<String, Object> map);
    /**
     * 获取离线消息
     * @param map
     * @return
     */
    List<UserMessageEntity> getOfflineMessageList(Map<String, Object> map);
    /**
     * 获取历史记录总条数
     * @param map
     * @return
     */
    int getHistoryMessageCount(Map<String, Object> map);
}
