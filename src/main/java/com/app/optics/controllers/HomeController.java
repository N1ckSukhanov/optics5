package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.products.*;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final AppService appService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OptionService optionService;
    private final PhotoService photoService;

    @GetMapping("")
    public String home(@RequestParam(required = false, defaultValue = "") String search,
                       Model model) {
        if (!customerService.getLastDelete().isBlank()) {
            search = customerService.getLastDelete();
            customerService.setLastDelete("");
        }

        if (!search.isBlank()) {
            appService.setAppState(AppState.CUSTOMERS);
            customerService.setCurrent(null);
        }

        model.addAttribute("search", search);
        model.addAttribute("current", customerService.getCurrent());

        switch (appService.getAppState()) {
            case PRODUCTS -> {
                model.addAttribute("products", productService.getProductsByCurrent());
                model.addAttribute("showProducts", true);
                model.addAttribute("images", photoService.getImagesByCustomer());
            }
            case CUSTOMERS -> model.addAttribute("customers", customerService.getCustomersBySearch(search));
            case NEW_CUSTOMER -> model.addAttribute("customer", customerService.getCustomerFromSearch());
            case NEW_RECIPE -> optionService.addProductOptions(model, new Recipe());
            case NEW_LENSES -> optionService.addProductOptions(model, new Lenses());
            case NEW_OTHER -> {
                ProductType type = ProductType.valueOf(productService.getOtherType().toUpperCase());
                optionService.addProductOptions(model, new Other(type));
            }
            case CUSTOMER_FROM_RECIPE -> {
                model.addAttribute("customer", customerService.getLastCustomer());
                model.addAttribute("customers", customerService.findByNameOrPhone(customerService.getLastCustomer()));
            }
            case EDIT_PRODUCT -> {
                Product product = productService.getProductById(productService.getEditId());
                optionService.addProductOptions(model, product);
                AppState state = switch (product.getProductType()) {
                    case RECIPE -> AppState.NEW_RECIPE;
                    case LENSES -> AppState.NEW_LENSES;
                    default -> AppState.NEW_OTHER;
                };
                appService.setAppState(state);
            }
            case CHOOSE_OPTION -> {
                model.addAttribute("option", new Option());
                model.addAttribute("chooseOptions", optionService.getAllOptionTypes());
            }
            case NEW_OPTION -> {
                Option currentOption = optionService.getCurrentOption();
                model.addAttribute("option", new Option(currentOption.getType()));
                model.addAttribute("currentOption", currentOption);
                model.addAttribute("addedOptions", optionService.findAllByType(currentOption.getType()));
            }
            case NOT_FOUND -> {
                model.addAttribute("my_path", customerService.getNotFound());
                model.addAttribute("products", productService.getProductsByCurrent());
                model.addAttribute("showProducts", true);
                model.addAttribute("images", photoService.getImagesByCustomer());
            }
        }

        model.addAttribute("appState", appService.getAppState());

        return "home";
    }

}




