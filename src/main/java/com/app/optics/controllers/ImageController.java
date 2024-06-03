package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.app.optics.util.Constants.HOME;
import static com.app.optics.util.Constants.TO_PRODUCTS;

@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final AppService appService;
    private final PhotoService photoService;

    @GetMapping("/upload")
    public String uploadPage(Model model) {
        appService.setModelState(AppState.UPLOAD, model);
        return HOME;
    }

    @PostMapping("/upload")
    public String uploadRecipe(@RequestParam MultipartFile file) {
        photoService.save(file);
        return TO_PRODUCTS;
    }

    @GetMapping("/delete/{imageId}")
    public String deleteImage(@PathVariable Integer imageId) {
        photoService.delete(imageId);
        return TO_PRODUCTS;
    }
}
