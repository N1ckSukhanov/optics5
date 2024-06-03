package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.Customer;
import com.app.optics.services.AppService;
import com.app.optics.services.CustomerService;
import com.app.optics.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.app.optics.util.Constants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final AppService appService;
    private final CustomerService customerService;
    private final ProductService productService;

    @GetMapping
    public String customers(@RequestParam(required = false, defaultValue = "") String search, Model model) {
        appService.setModelState(AppState.CUSTOMERS, model);

        search = customerService.getActualSearch(search);

        model.addAttribute("current", customerService.getCurrent());
        model.addAttribute("search", search);

        model.addAttribute("customers", customerService.getCustomersBySearch(search));
        return HOME;
    }

    /**
     * Find customers from recipe's name & phone fields, fill that fields into customer
     */
    @GetMapping("/from_recipe")
    public String createCustomerGet(Model model) {
        appService.setModelState(AppState.CUSTOMER_FROM_RECIPE, model);
        model.addAttribute("customer", customerService.getLastCustomer());
        model.addAttribute("customers", customerService.findByNameOrPhone(customerService.getLastCustomer()));
        return HOME;
    }

    /**
     * Find customers from recipe's name & phone fields, fill that fields into customer
     */
    @PostMapping("/from_recipe")
    public String createRecipeCustomerPost(@ModelAttribute Customer customer) {
        customerService.setLastCustomer(customer);
        return "redirect:/customer/from_recipe";
    }

    /**
     * Customer has name / phone from search
     */
    @GetMapping("/create")
    public String createCustomer(Model model) {
        appService.setModelState(AppState.NEW_CUSTOMER, model);
        model.addAttribute("customer", customerService.getCustomerFromSearch());
        return HOME;
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
        // todo 2 actions in one method
        customerService.setLastCustomer(null);
        // todo last customer && delete customer in 1
        return TO_PRODUCTS;
    }

    /**
     * Set current customer, then show products to move them from cart to orders
     */
    @GetMapping("/choose/{id}")
    public String chooseCustomer(@PathVariable int id) {
        customerService.setCurrentId(id);
        productService.putCartToOrders();
        productService.putImagesToCustomer();
        return TO_PRODUCTS;
    }

    /**
     * Set last delete to keep search same when reload page
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id, @RequestParam String search) {
        productService.deleteProductsByCustomer(id);
        customerService.deleteCustomerById(id);
        customerService.setLastSearch(search);
        return TO_CUSTOMERS;
    }

    @GetMapping("/clear_choose")
    public String clearChosenCustomer() {
        customerService.setCurrent(null);
        return TO_PRODUCTS;
    }

}
