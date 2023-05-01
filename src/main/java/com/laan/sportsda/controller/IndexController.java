package com.laan.sportsda.controller;

import com.laan.sportsda.util.MessagesUtil;
import com.laan.sportsda.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathUtil.INIT)
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Object> init() {
        log.info("initializing request received");
        String message = messageSource.getMessage(MessagesUtil.WELCOME_MESSAGE, null, LocaleContextHolder.getLocale());
        log.info("initialized status: {}", message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
