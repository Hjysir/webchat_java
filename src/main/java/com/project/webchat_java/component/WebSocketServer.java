package com.project.webchat_java.component;

import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.service.ChatRoomService;
import com.project.webchat_java.service.RedisService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint("/serve/{chatRoomName}/{loginName}")
public class WebSocketServer {
    private static RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private static Map<String, Set<WebSocketServer>> chatRoomSubscribeListenerMap = new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<String> chatRoomSet = new CopyOnWriteArraySet<String>();
    private Session userSession;
    private String loginName = "";
    private SubscribeListener subscribeListener;

    @OnOpen
    public void onOpen(@PathParam("chatRoomName") String chatRoomName, @PathParam("loginName") String loginName, Session session) {
        this.loginName = loginName;
        for (WebSocketServer item : webSocketSet) {
            if (item.loginName.equals(loginName)) return; // 如果已经存在则不再添加
        }

        this.userSession = session; //加入set中
        webSocketSet.add(this); //在线数加1
        addOnlineCount();

        subscribeListener = ApplicationContextProvider.getBean(SubscribeListener.class);
        subscribeListener.setSession(session);

        if (chatRoomSubscribeListenerMap.containsKey(chatRoomName)) {
            chatRoomSubscribeListenerMap.get(chatRoomName).add(this);
        } else {
            Set<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<WebSocketServer>();
            webSocketServers.add(this);
            chatRoomSubscribeListenerMap.put(chatRoomName, webSocketServers);
        }

        log.info("聊天室[" + chatRoomName + "] 有新连接 [" + loginName + "]加入！当前在线人数为{}", getOnlineCount());

        ChatRoomService chatRoomService =  ApplicationContextProvider.getBean(ChatRoomService.class);
        RedisService redisUtil = ApplicationContextProvider.getBean(RedisService.class);

        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(loginName);
        if (!chatRooms.isEmpty()) {
            for (ChatRoom room : chatRooms) {
                chatRoomSet.add(room.getId());
                if (chatRoomSubscribeListenerMap.containsKey(room.getId())) {
                    chatRoomSubscribeListenerMap.get(room.getId()).add(this);
                } else {
                    Set<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<WebSocketServer>();
                    webSocketServers.add(this);
                    chatRoomSubscribeListenerMap.put(room.getId(), webSocketServers);
                }
                redisUtil.publish(room.getId(), "欢迎" + loginName + "加入聊天室");
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        ChatRoomService chatRoomService =  ApplicationContextProvider.getBean(ChatRoomService.class);
        RedisService redisUtil = ApplicationContextProvider.getBean(RedisService.class);

        log.info("来自客户端[" + loginName + "]的消息:" + message);
        if (!chatRoomSet.isEmpty()) {
            for (String chatRoomId : chatRoomSet) {
                log.info("Publishing to chatroom: " + chatRoomId);
                redisUtil.publish(chatRoomId, loginName + ":" + message);
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为{}", getOnlineCount());
        // 找到这个用户订阅的所有聊天室
        for (String chatRoomId : chatRoomSet) {
            redisMessageListenerContainer.removeMessageListener(subscribeListener, new ChannelTopic("chatroom:" + chatRoomId));
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误，{}", error);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.userSession.getBasicRemote().sendText(message);
    }

    public int getOnlineCount() {
        return onlineCount.get();
    }

    public void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }

}