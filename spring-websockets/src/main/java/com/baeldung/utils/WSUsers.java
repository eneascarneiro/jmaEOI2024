package com.baeldung.utils;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
public class WSUsers {
    private List<UserWS> userWSList;

    public WSUsers() {
        if (this.userWSList == null)
            this.userWSList = new ArrayList<>();
    }

    public void addUserWS (String username, String uuid){
        System.out.println("addUserWS username:" + username);
        System.out.println("addUserWS uuid:" + uuid);
        userWSList.add(new UserWS(username, uuid));
    }
    public String getUUIdforUser(String username){
        String uuid = "";
        System.out.println("getUUIdforUser username:" + username);
        for (UserWS itm : userWSList) {
            if ( itm.getUsername().equals(username)){
                uuid = itm.getUid();
            }
        }
        System.out.println(" getUUIdforUser uuid:" + uuid);
        return uuid;
    }
    public Integer  countUser(){
        return userWSList.size();
    }
    public Integer  check(){
        if (this.userWSList == null)
            return 0;
        else
            return 1;
    }
}
