package com.baeldung.sendtouser;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageTo {

    private String from;

    private String to;

    private String text;


}
