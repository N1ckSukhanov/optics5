package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.products.Option;
import com.app.optics.services.AppService;
import com.app.optics.services.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.app.optics.util.Constants.HOME;
import static com.app.optics.util.Constants.TO_OPTIONS;

@Controller
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;
    private final AppService appService;

    @GetMapping()
    public String options(Model model) {
        // todo why it was used ?
//        Option current = optionService.getCurrentOption();
//        AppState state = current != null ? AppState.NEW_OPTION : AppState.CHOOSE_OPTION;
        appService.setModelState(AppState.CHOOSE_OPTION, model);
        model.addAttribute("option", new Option());
        model.addAttribute("chooseOptions", optionService.getAllOptionTypes());
        return HOME;
    }

    @PostMapping("/choose")
    public String chooseOption(@ModelAttribute Option option, Model model) {
        optionService.setCurrentOption(option);
        appService.setModelState(AppState.NEW_OPTION, model);
        model.addAttribute("option", new Option(option.getType()));
        model.addAttribute("currentOption", option);
        model.addAttribute("addedOptions", optionService.findAllByType(option.getType()));
        return HOME;
    }

    @PostMapping("/add")
    public String addOption(@ModelAttribute Option option) {
        optionService.addOption(option);
        return TO_OPTIONS;
    }

    @GetMapping("/delete/{id}")
    public String deleteOption(@PathVariable int id) {
        optionService.deleteById(id);
        return TO_OPTIONS;
    }

    @GetMapping("/clear")
    public String clearChosenOption() {
        optionService.setCurrentOption(null);
        return TO_OPTIONS;
    }
}
