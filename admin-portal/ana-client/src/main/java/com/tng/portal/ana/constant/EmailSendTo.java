package com.tng.portal.ana.constant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Owen on 2017/8/10.
 */
public enum EmailSendTo  implements Serializable{

    Agent("A"),
    User("U");

    private String code;

    EmailSendTo(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public static EmailSendTo parse(String code){
        Optional<EmailSendTo> optional = Arrays.asList(EmailSendTo.values()).stream().filter(item->item.getCode().equals(code)).findFirst();
        return optional.isPresent()?optional.get():null;
    }

    public static String getName(String code){
        EmailSendTo em  = parse(code);
        return null==em?"":em.name();
    }

}
