package com.laan.sportsda.controller;

import com.laan.sportsda.util.MessagesUtil;
import com.laan.sportsda.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathUtil.INIT)
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final MessageSource messageSource;

    public IndexController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<Object> init() {
        logger.info("initializing request received");
        String message = messageSource.getMessage(MessagesUtil.WELCOME_MESSAGE, null, LocaleContextHolder.getLocale());
        logger.info("initialized status: {}", message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
