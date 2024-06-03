package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.Customer;
import com.app.optics.models.Photo;
import com.app.optics.models.Print;
import com.app.optics.models.products.Lenses;
import com.app.optics.models.products.Other;
import com.app.optics.models.products.Product;
import com.app.optics.models.products.ProductType;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.app.optics.util.Constants.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CustomerService customerService;
    private final AppService appService;
    private final PhotoService photoService;
    private final OptionService optionService;
    private final DiscountService discountService;

    @GetMapping()
    public String products(Model model) {
        appService.setModelState(AppState.PRODUCTS, model);
        customerService.processNotFound(model);
        model.addAttribute("products", productService.getProductsByCurrent());
        model.addAttribute("images", photoService.getImagesByCurrent());
        return HOME;
    }

    @GetMapping("/print/{id}")
    public String printRecipe(@PathVariable int id, Model model) {
        model.addAttribute("product", (Recipe) productService.getProductById(id));
        model.addAttribute("customer", customerService.getCurrent());
        model.addAttribute("print", new Print());
        return "print/all";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        productService.setEditId(id);
        Product product = productService.getProductById(productService.getEditId());
        optionService.addProductOptions(model, product);
        AppState state = switch (product.getProductType()) {
            case RECIPE -> AppState.NEW_RECIPE;
            case LENSES -> AppState.NEW_LENSES;
            default -> AppState.NEW_OTHER;
        };
        appService.setModelState(state, model);
        return HOME;
    }

    @GetMapping("/open/{id}")
    public String openProduct(@PathVariable int id) {
        productService.setProductOpen(id);
        return TO_PRODUCTS;
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return TO_PRODUCTS;
    }

    @GetMapping("/recipe")
    public String createRecipe(Model model) {
        appService.setModelState(AppState.NEW_RECIPE, model);
        Recipe recipe = new Recipe();
        recipe.setDiscountPercent(discountService.getCustomerDiscount(customerService.getCurrent()));
        // todo method to set discount
        optionService.addProductOptions(model, recipe);
        return HOME;
    }

    @GetMapping("/recipe/old")
    public String createRecipeOld(Model model) {
        // todo old and new recipe in one method
        appService.setModelState(AppState.OLD_RECIPE, model);
        Recipe recipe = new Recipe();
        recipe.setDiscountPercent(0);
        optionService.addProductOptions(model, recipe);
        return HOME;
    }

    @PostMapping("/recipe")
    public String addRecipe(@ModelAttribute Recipe recipe, Model model) {
        recipe.setDiscountFromOthers();
        productService.putProduct(recipe);
        // todo create customer by recipe
        String name = recipe.getCustomerName();
        String phone = recipe.getCustomerPhone();

        if (customerService.isCurrent() || (name.isBlank() && phone.isBlank())) {
            appService.setModelState(AppState.PRODUCTS, model);
            return TO_PRODUCTS;
        } else {;
            customerService.setLastCustomer(new Customer(name, phone));
            return "redirect:/customers/from_recipe";
        }
    }

    @GetMapping("/lenses")
    public String createLenses(Model model) {
        appService.setModelState(AppState.NEW_LENSES, model);
        optionService.addProductOptions(model, new Lenses());
        return HOME;
    }

    @PostMapping("/lenses")
    public String addLenses(@ModelAttribute Lenses lenses) {
        productService.putProduct(lenses);
        return TO_PRODUCTS;
    }

    @GetMapping("/other/{type}")
    public String createOther(@PathVariable String type, Model model) {
        appService.setModelState(AppState.NEW_OTHER, model);
        optionService.addProductOptions(model, new Other(ProductType.valueOf(type.toUpperCase())));
        return HOME;
    }

    @PostMapping("/other")
    public String addOther(@ModelAttribute Other other) {
        productService.putProduct(other);
        return TO_PRODUCTS;
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource downloadImage(@PathVariable Integer imageId) {
        return new ByteArrayResource(photoService.getPhoto(imageId).getData());
    }
}
