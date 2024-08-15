package com.baeldung.sendtouser;

import com.baeldung.utils.UserWS;
import com.baeldung.utils.WSUsers;
import com.baeldung.websockets.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebsocketSendToUserController {

    private static final  WSUsers wsUsers = new WSUsers();
    private final SimpUserRegistry simpUserRegistry;
    @Autowired
    private NotificationService notificationService;

    public WebsocketSendToUserController(  SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }

    @GetMapping("/registerwebsocket/{user}")
    public void GetNumberOfUsers(@PathVariable("user") String user){
        System.out.println("user");
        System.out.println(user);
        List<String> stringList =  this.simpUserRegistry
                .getUsers()
                .stream()
                .map(SimpUser::getName)
                .collect(Collectors.toList());
        System.out.println(stringList.stream().toArray());
        System.out.println("size :");
        System.out.println(stringList.size());
        String uuid = "";
        for (String itm : stringList) {
            uuid = itm;
        }
        wsUsers.addUserWS(user,uuid);


        System.out.println(wsUsers.countUser());
    }

    /**
     * Example of sending message to specific user using 'convertAndSendToUser()' and '/queue'
     */
    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public OutputMessage getPrivateMessage(final MessageTo message,
                                           final Principal principal) throws InterruptedException {
        //Actualizo la lista de usuarios


        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println("principal.getName()");
        System.out.println(principal.getName());
        Thread.sleep(100);
        notificationService.sendPrivateNotification(wsUsers.getUUIdforUser(message.getTo()).toString(), message.getText(), message.getFrom());
        return new OutputMessage(message.getFrom(), "Sending private message to user " + message.getTo() + ": "
                + message.getText(), time);
    }
}
