package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.app.optics.util.Constants.HOME;
import static com.app.optics.util.Constants.TO_PRODUCTS;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {
    private final CustomerService customerService;

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable thr) {
        customerService.setNotFound(true);
        thr.printStackTrace();
        log.error("not found", thr);
        return TO_PRODUCTS;
    }
}
