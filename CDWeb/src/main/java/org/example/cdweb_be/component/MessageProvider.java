package org.example.cdweb_be.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Component
public class MessageProvider {

    @Autowired
    private MessageSource messageSource;
    private static HashMap<String, Locale> mapLocal = new HashMap<>();
    private Locale defaultLocal =  new Locale("vi", "VN");
    static{
        mapLocal.put("vi", new Locale("vi", "VN"));
        mapLocal.put("en", Locale.ENGLISH);
    }
    private Locale locale =  new Locale("vi", "VN"); // Ngôn ngữ mặc định

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public void setLocale(String language){
        Locale newLocale = mapLocal.get(language);
        this.locale = (newLocale != null)?newLocale:defaultLocal;
//        log.info("Local: "+this.locale.getCountry());
    }
    public Locale getLocale() {
        return locale;
    }
}