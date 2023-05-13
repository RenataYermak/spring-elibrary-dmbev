package org.example.service.http.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@ControllerAdvice(basePackages = "org.example.service.http.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handlerException(Exception exception) {
        if (!Objects.equals(exception, new ResponseStatusException(HttpStatus.NOT_FOUND))) {
            log.error("Not found element", exception);
            return "error/error404";
        } else {
            log.error("Failed to return response", exception);
            return "error/error500";
        }
    }
}
