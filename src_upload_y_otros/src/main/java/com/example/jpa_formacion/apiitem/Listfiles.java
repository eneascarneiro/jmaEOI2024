package com.example.jpa_formacion.apiitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Listfiles {
    private Integer key;
    private String id ;
    private String name ;
    private String s3Path ;
    private String modificationDate ;
    private String footprint;
    private String geofootprint;
    private String online ;
    private String originDate;
    private String publicationDate;
    private String reference;
    private String userid;
    private String groupid;

}
