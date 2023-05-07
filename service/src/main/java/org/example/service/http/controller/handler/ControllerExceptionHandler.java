package org.example.service.http.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "org.example.service.http.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handlerException(Exception exception){
        log.error("Failed to return response", exception);
        return "error/error500";
    }
}
