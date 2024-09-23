package com.atelier.common.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TranslationService {

    private final MessageSource messageSource;

    public TranslationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Locale locale) {
        try {
            String message = messageSource.getMessage(code, null, locale);
            System.out.println("Message for code: " + code + " and locale: " + locale + " is: " + message);
            return message;
        } catch (NoSuchMessageException e) {
            System.out.println("No message found for code: " + code + " and locale: " + locale);
            return "Message not found";
        }
    }
}
