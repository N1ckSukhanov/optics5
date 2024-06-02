package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.services.AppService;
import com.app.optics.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.app.optics.util.Constants.HOME;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {
    private final AppService appService;
    private final CustomerService customerService;

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable thr) {
        appService.setAppState(AppState.NOT_FOUND);
        customerService.setNotFound("error");
        thr.printStackTrace();
        log.error("not found", thr);
        return HOME;
    }
}
