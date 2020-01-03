package com.pit.im.server.model.session.impl;

import com.pit.im.constants.ImConstants;
import com.pit.im.server.group.ImChannelGroup;
import com.pit.im.server.model.MessageProto;
import com.pit.im.server.model.MessageWrapper;
import com.pit.im.server.model.Session;
import com.pit.im.server.model.session.SessionManger;
import com.pit.im.server.proxy.MessageProxy;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.directwebremoting.ScriptSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author deng
 * @date 2020/1/3 15:10
 */
@Slf4j
@Service
public class SessionMangerImpl implements SessionManger {


    @Autowired
    private MessageProxy messageProxy;
    /**
     * The set of currently active Sessions for this Manager, keyed by session
     * identifier.
     */
    protected Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    @Override
    public synchronized void addSession(Session session) {
        if (null == session) {
            return;
        }
        sessions.put(session.getAccount(), session);
        if (session.getSource() != ImConstants.ImserverConfig.DWR) {
            ImChannelGroup.add(session.getSession());
        }
        //全员发送上线消息
        MessageProto.Model model = messageProxy.getOnLineStateMsg(session.getAccount());
        ImChannelGroup.broadcast(model);

        log.debug("put a session " + session.getAccount() + " to sessions!");
        log.debug("session size " + sessions.size());

    }

    @Override
    public synchronized void updateSession(Session session) {
        session.setUpdateTime(System.currentTimeMillis());
        sessions.put(session.getAccount(), session);
    }

    @Override
    public synchronized void removeSession(String sessionId) {
        try {
            Session session = getSession(sessionId);
            if (session != null) {
                session.closeAll();
                sessions.remove(sessionId);
                MessageProto.Model model = messageProxy.getOffLineStateMsg(sessionId);
                ImChannelGroup.broadcast(model);
            }
        } catch (Exception e) {

        }
        log.debug("session size " + sessions.size());
        log.info("system remove the session " + sessionId + " from sessions!");
    }

    @Override
    public synchronized void removeSession(String sessionId, String nid) {
        try {
            Session session = getSession(sessionId);
            if (session != null) {
                int source = session.getSource();
                if (source == ImConstants.ImserverConfig.WEBSOCKET || source == ImConstants.ImserverConfig.DWR) {
                    session.close(nid);
                    //判断没有其它session后 从SessionManager里面移除
                    if (session.otherSessionSize() < 1) {
                        sessions.remove(sessionId);
                        MessageProto.Model model = messageProxy.getOffLineStateMsg(sessionId);
                        ImChannelGroup.broadcast(model);
                    }
                } else {
                    session.close();
                    sessions.remove(sessionId);
                    MessageProto.Model model = messageProxy.getOffLineStateMsg(sessionId);
                    ImChannelGroup.broadcast(model);
                }
            }
        } catch (Exception e) {

        }
        log.info("remove the session " + sessionId + " from sessions!");
    }

    @Override
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public Session[] getSessions() {
        return sessions.values().toArray(new Session[0]);
    }

    @Override
    public Set<String> getSessionKeys() {
        return sessions.keySet();
    }

    @Override
    public int getSessionCount() {
        return sessions.size();
    }

    @Override
    public Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx) {
        String sessionId = wrapper.getSessionId();
        Session session = sessions.get(sessionId);
        if (session != null) {
            log.info("session " + sessionId + " exist!");
            //当链接来源不是同一来源或者 是socket链接，踢掉已经登录的session
            if (session.getSource() == ImConstants.ImserverConfig.SOCKET) {
                // 如果session已经存在则销毁session
                //从广播组清除
                log.info("sessionId" + session.getNid() + "------------------" + ctx.channel().id().asShortText() + "      !");
                ImChannelGroup.remove(session.getSession());
                session.close(session.getNid());
                sessions.remove(session.getAccount());
                log.info("session " + sessionId + " have been closed!");
            } else if (session.getSource() == ImConstants.ImserverConfig.WEBSOCKET) {
                //用于解决websocket多开页面session被踢下线的问题
                Session newsession = setSessionContent(ctx, wrapper, sessionId);
                session.addSessions(newsession);
                updateSession(session);
                ImChannelGroup.add(newsession.getSession());
                log.info("session " + sessionId + " update!");
                return newsession;
            } else if (session.getSource() == ImConstants.ImserverConfig.DWR) {
                //清除dwr session
                log.info("sessionId ----" + session.getAccount() + " start remove !");
                session.closeAll();
                sessions.remove(session.getAccount());
                log.info("session " + sessionId + " have been closed!");
            }
        }
        session = setSessionContent(ctx, wrapper, sessionId);
        addSession(session);
        return session;
    }

    @Override
    public Session createSession(ScriptSession scriptSession, String sessionid) {
        Session dwrsession = new Session(scriptSession);
        dwrsession.setAccount(sessionid);
        dwrsession.setPlatform((String) scriptSession.getAttribute(ImConstants.DWRConfig.BROWSER));
        dwrsession.setPlatformVersion((String) scriptSession.getAttribute(ImConstants.DWRConfig.BROWSER_VERSION));
        dwrsession.setBindTime(System.currentTimeMillis());
        dwrsession.setUpdateTime(System.currentTimeMillis());
        Session session = sessions.get(sessionid);
        if (session != null) {
            log.info("session " + sessionid + " exist!");
            if (session.getSource() != ImConstants.ImserverConfig.DWR) {
                //从广播组清除
                log.info("sessionId ----" + session.getAccount() + " start remove !");
                ImChannelGroup.remove(session.getSession());
                List<Channel> channels = session.getSessionAll();
                if (channels != null && channels.size() > 0) {
                    for (Channel cl : channels) {
                        ImChannelGroup.remove(cl);
                    }
                }
                //session.close();
                sessions.remove(session.getAccount());
                log.info("session " + sessionid + " have been closed!");
            } else if (session.getSource() == ImConstants.ImserverConfig.DWR) {
                session.addSessions(dwrsession);
                updateSession(session);
                log.info("session " + sessionid + " update!");
                return dwrsession;
            }
        }
        addSession(dwrsession);
        return dwrsession;
    }

    @Override
    public boolean exist(String sessionId) {
        Session session = getSession(sessionId);
        return session != null ? true : false;
    }

    /**
     * 设置session内容
     *
     * @param ctx
     * @param wrapper
     * @param sessionId
     * @return
     */
    private Session setSessionContent(ChannelHandlerContext ctx, MessageWrapper wrapper, String sessionId) {
        log.info("create new session " + sessionId + ", ctx -> " + ctx.toString());
        MessageProto.Model model = (MessageProto.Model) wrapper.getBody();
        Session session = new Session(ctx.channel());
        session.setAccount(sessionId);
        session.setSource(wrapper.getSource());
        session.setAppKey(model.getAppKey());
        session.setDeviceId(model.getDeviceId());
        session.setPlatform(model.getPlatform());
        session.setPlatformVersion(model.getPlatformVersion());
        session.setSign(model.getSign());
        session.setBindTime(System.currentTimeMillis());
        session.setUpdateTime(session.getBindTime());
        log.info("create new session " + sessionId + " successful!");
        return session;
    }
}
