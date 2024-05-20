package com.app.optics.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] downloadFile() throws IOException {
        return Files.readAllBytes(Path.of("/data/optics.mv.db"));
    }
}
