package org.example.service.http.controller.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "org.example.service.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
