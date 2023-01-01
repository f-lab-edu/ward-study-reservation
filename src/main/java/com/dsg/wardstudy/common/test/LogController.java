package com.dsg.wardstudy.common.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class LogController {

    @GetMapping(value = "/log")
    public String log() throws Exception {

        //FATAL, ERROR, WARN, INFO, DEBUG, TRACE
        log.fatal("FATAL");
        log.error("ERROR");
        log.warn("WARN");
        log.info("INFO");
        log.debug("DEBUG");
        log.trace("TRACE");

        return "log";
    }

}
