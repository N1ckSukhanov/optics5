package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.Customer;
import com.app.optics.services.AppService;
import com.app.optics.services.CustomerService;
import com.app.optics.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final AppService appService;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Customer has name / phone from search
     */
    @GetMapping("/create")
    public String createCustomer() {
        appService.setAppState(AppState.NEW_CUSTOMER);
        return "redirect:/";
    }

    /**
     * Set current customer, then show products to move them from cart to orders
     */
    @PostMapping("/add")
    public String addCustomer(@ModelAttribute Customer customer) {
        customerService.createCustomer(customer);
        customerService.setCurrent(customer);
        productService.putCartToOrders();
        productService.putImagesToCustomer();
        appService.setAppState(AppState.PRODUCTS);
        customerService.setLastCustomer(null);
        return "redirect:/";
    }

    /**
     * Set current customer, then show products to move them from cart to orders
     */
    @GetMapping("/choose/{id}")
    public String chooseCustomer(@PathVariable int id) {
        customerService.setCurrentId(id);
        productService.putCartToOrders();
        productService.putImagesToCustomer();
        appService.setAppState(AppState.PRODUCTS);
        return "redirect:/";
    }

    /**
     * Set last delete to keep search same when reload page
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id, @RequestParam String search) {
        productService.deleteProductsByCustomer(id);
        customerService.deleteCustomerById(id);
        customerService.setLastDelete(search);
        return "redirect:/";
    }

    @GetMapping("/clear_choose")
    public String clearChosenCustomer() {
        customerService.setCurrent(null);
        appService.setAppState(AppState.PRODUCTS);
        return "redirect:/";
    }

    /**
     * Find customers from recipe's name & phone fields, fill that fields into customer
     */
    @PostMapping("/from_recipe")
    public String createRecipeCustomerPost(@ModelAttribute Customer customer) {
        customerService.setLastCustomer(customer);
        appService.setAppState(AppState.CUSTOMER_FROM_RECIPE);
        return "redirect:/";
    }
}
