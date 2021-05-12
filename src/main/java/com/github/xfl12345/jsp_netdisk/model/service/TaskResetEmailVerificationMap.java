package com.github.xfl12345.jsp_netdisk.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskResetEmailVerificationMap {

    private final Logger logger= LoggerFactory.getLogger(TaskResetEmailVerificationMap.class);

    @Autowired
    private EmailVerificationService emailVerificationService;

    public void reset(){
        logger.info("Start to reset emailVerificationMap...");
        try {
            emailVerificationService.emailVerificationMap.clear();
            logger.info("Reset emailVerificationMap finished.");
        }
        catch (UnsupportedOperationException e){
            logger.error("Reset emailVerificationMap failed.Error=" + e);
        }
    }
}
