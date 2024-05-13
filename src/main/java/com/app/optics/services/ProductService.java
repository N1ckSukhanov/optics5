package com.app.optics.services;

import com.app.optics.models.Customer;
import com.app.optics.models.Photo;
import com.app.optics.models.products.Lenses;
import com.app.optics.models.products.Other;
import com.app.optics.models.products.Product;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.repositories.PhotoRepository;
import com.app.optics.repositories.products.LensesRepository;
import com.app.optics.repositories.products.OtherRepository;
import com.app.optics.repositories.products.RecipeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final RecipeRepository recipeRepository;
    private final LensesRepository lensesRepository;
    private final OtherRepository otherRepository;
    private final PhotoRepository photoRepository;

    private final CustomerService customerService;
    private boolean showProducts = true;
    private final Set<Integer> openedProducts = new HashSet<>();
    @Getter
    @Setter
    public String otherType;
    @Getter
    @Setter
    public Integer editId;

    public List<Product> getProductsByCustomerId(Integer id) {
        List<Product> products = new ArrayList<>();
        products.addAll(recipeRepository.findByCustomerId(id));
        products.addAll(lensesRepository.findByCustomerId(id));
        products.addAll(otherRepository.findByCustomerId(id));
        products = products.stream().sorted().toList();
        LocalDate date = null;
        for (Product product : products) {
            if (openedProducts.contains(product.getId())) {
                product.setOpened(true);
            }
            if (product.getReceptionDate() != null && (date == null || product.getReceptionDate().isBefore(date))) {
                date = product.getReceptionDate();
                product.setNewLine(true);
            } else {
                product.setNewLine(false);
            }
        }
        return products;
    }

    public List<Product> getProductsByCurrent() {
        return getProductsByCustomer(customerService.getCurrent());
    }

    public List<Product> getProductsByCustomer(Customer customer) {
        Integer id = customer != null ? customer.getId() : null;
        return getProductsByCustomerId(id);
    }

    @Transactional
    public void deleteProductsByCustomer(Integer customerId) {
        recipeRepository.deleteAllByCustomerId(customerId);
        lensesRepository.deleteAllByCustomerId(customerId);
        otherRepository.deleteAllByCustomerId(customerId);
        photoRepository.deleteAllByCustomerId(customerId);
    }

    public void setProductOpen(int id) {
        if (openedProducts.contains(id))
            openedProducts.remove(id);
        else
            openedProducts.add(id);
    }


    @Transactional
    public void putProduct(Product product) {
        if (customerService.isCurrent() && product.getCustomerId() == null) {
            Integer customerId = customerService.getCurrentId();
            product.setCustomerId(customerId);
            product.setSerialNumber(recipeRepository.findByCustomerId(customerId).size()
                    + photoRepository.findByCustomerId(customerId).size() + 1);
            System.out.println("product.getSerialNumber() = " + product.getSerialNumber());
        }

        switch (product.getProductType()) {
            case RECIPE -> recipeRepository.save((Recipe) product);
            case LENSES -> lensesRepository.save((Lenses) product);
            default -> otherRepository.save((Other) product);
        }
    }

    public Product getProductById(int id) {
        if (recipeRepository.existsById(id))
            return recipeRepository.findById(id).orElse(null);
        if (lensesRepository.existsById(id))
            return lensesRepository.findById(id).orElse(null);
        if (otherRepository.existsById(id))
            return otherRepository.findById(id).orElse(null);
        return null;
    }

    @Transactional
    public void deleteProductById(int id) {
        if (recipeRepository.existsById(id))
            recipeRepository.deleteById(id);
        if (lensesRepository.existsById(id))
            lensesRepository.deleteById(id);
        if (otherRepository.existsById(id))
            otherRepository.deleteById(id);
    }

    @Transactional
    public void putCartToOrders() {
        openedProducts.clear();
        if (customerService.isCurrent()) {
            List<Product> products = getProductsByCustomerId(null);
            products.forEach(product -> {
                product.setCustomerId(customerService.getCurrentId());
                putProduct(product);
            });
        }
    }

    @Transactional
    public void putImagesToCustomer() {
        if (customerService.isCurrent()) {
            List<Photo> photos = photoRepository.findByCustomerId(null);
            photos.forEach(photo -> {
                photo.setCustomerId(customerService.getCurrentId());
                photoRepository.save(photo);
            });
        }
    }


}
