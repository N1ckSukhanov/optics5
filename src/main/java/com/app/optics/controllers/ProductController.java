package com.app.optics.controllers;

import com.app.optics.models.AppState;
import com.app.optics.models.Customer;
import com.app.optics.models.Photo;
import com.app.optics.models.Print;
import com.app.optics.models.products.Lenses;
import com.app.optics.models.products.Other;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.services.AppService;
import com.app.optics.services.CustomerService;
import com.app.optics.services.PhotoService;
import com.app.optics.services.ProductService;
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

import static com.app.optics.util.Constants.HOME;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CustomerService customerService;
    private final AppService appService;
    private final PhotoService photoService;

    @GetMapping("/upload")
    public String uploadPage() {
        return "product/upload";
    }

    @PostMapping("/upload")
    public String uploadRecipe(@RequestParam MultipartFile file) {
        System.out.println(file.getName() + file.getOriginalFilename() + file.getSize());
        photoService.save(file);
        return HOME;
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable int id) {
        Photo photo = photoService.getPhoto(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getName() + "\"")
                .body(photo.getData());
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource downloadImage(@PathVariable Integer imageId) {
        return new ByteArrayResource(photoService.getPhoto(imageId).getData());
    }

    @GetMapping(value = "/images/{imageId}/delete")
    public String deleteImage(@PathVariable Integer imageId) {
        System.out.println("delete image" + imageId);
        photoService.delete(imageId);
        return HOME;
    }

    @GetMapping("/print/{id}")
    public String printRecipe(@PathVariable int id, Model model) {
        appService.setAppState(AppState.PRODUCTS);
        model.addAttribute("product", (Recipe) productService.getProductById(id));
        model.addAttribute("customer", customerService.getCurrent());
        model.addAttribute("print", new Print());
        return "product/print_recipe";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable int id) {
        productService.setEditId(id);
        appService.setAppState(AppState.EDIT_PRODUCT);
        return HOME;
    }

    @GetMapping("/open/{id}")
    public String openProduct(@PathVariable int id) {
        productService.setProductOpen(id);
        return HOME;
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return HOME;
    }

    @GetMapping("/recipe")
    public String createRecipe() {
        appService.setAppState(AppState.NEW_RECIPE);
        return HOME;
    }

    @PostMapping("/recipe")
    public String addRecipe(@ModelAttribute Recipe recipe) {
        recipe.setDiscountFromOthers();
        productService.putProduct(recipe);
        String name = recipe.getCustomerName();
        String phone = recipe.getCustomerPhone();

        if (customerService.isCurrent() || (name.isBlank() && phone.isBlank())) {
            appService.setAppState(AppState.PRODUCTS);
        } else {
            customerService.setLastCustomer(new Customer(name, phone));
            appService.setAppState(AppState.CUSTOMER_FROM_RECIPE);
        }
        return HOME;
    }

    @GetMapping("/lenses")
    public String createLenses() {
        appService.setAppState(AppState.NEW_LENSES);
        return HOME;
    }

    @PostMapping("/lenses")
    public String addLenses(@ModelAttribute Lenses lenses) {
        productService.putProduct(lenses);
        appService.setAppState(AppState.PRODUCTS);
        return HOME;
    }

    @GetMapping("/other/{type}")
    public String createOther(@PathVariable String type) {
        productService.setOtherType(type);
        appService.setAppState(AppState.NEW_OTHER);
        return HOME;
    }

    @PostMapping("/other")
    public String addOther(@ModelAttribute Other other) {
        productService.putProduct(other);
        appService.setAppState(AppState.PRODUCTS);
        return HOME;
    }

    @GetMapping("/show_products")
    public String showProducts() {
        appService.setAppState(AppState.PRODUCTS);
        return HOME;
    }
}
