package com.project.webchat_java.component;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SubscribeListener implements MessageListener {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SubscribeListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    private Session session;


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        log.info("session = {}", session);
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String channel = new String(bytes);
        String messageContent = new String(message.getBody());
        String chatRoomId = channel.split(":")[1];
        log.info("收到消息，msg = {}", messageContent);
        if (ObjectUtils.isNotEmpty(session) && session.isOpen()) {
            simpMessagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, messageContent);
        }
    }
}
