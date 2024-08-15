package com.baeldung.sendtouser;

import com.baeldung.websockets.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage message = new OutputMessage("notifications", "Global Notification", time);

        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(String userId, String msg, String from) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage message = new OutputMessage(from, msg, time);
        messagingTemplate.convertAndSendToUser(userId,"/topic/private-messages", message);
    }
    public void sendPrivateNotificationRest(String userId, String msg) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage message = new OutputMessage("notification", msg, time);
        messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", message);
    }
}
