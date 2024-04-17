package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.products.Option;
import com.app.optics.services.AppService;
import com.app.optics.services.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.app.optics.util.Constants.HOME;

@Controller
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;
    private final AppService appService;

    @GetMapping()
    public String options() {
//        Option current = optionService.getCurrentOption();
//        AppState state = current != null ? AppState.NEW_OPTION : AppState.CHOOSE_OPTION;
        AppState state = AppState.CHOOSE_OPTION;
        appService.setAppState(state);
        return HOME;
    }

    @PostMapping("/choose")
    public String chooseOption(@ModelAttribute Option option) {
        optionService.setCurrentOption(option);
        appService.setAppState(AppState.NEW_OPTION);
        return HOME;
    }

    @PostMapping("/add")
    public String addOption(@ModelAttribute Option option) {
        optionService.addOption(option);
        return HOME;
    }

    @GetMapping("/delete/{id}")
    public String deleteOption(@PathVariable int id) {
        optionService.deleteById(id);
        return HOME;
    }

    @GetMapping("/clear")
    public String clearChosenOption() {
        optionService.setCurrentOption(null);
        appService.setAppState(AppState.CHOOSE_OPTION);
        return HOME;
    }
}
