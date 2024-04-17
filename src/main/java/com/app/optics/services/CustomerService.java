package com.app.optics.services;

import com.app.optics.models.Customer;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.repositories.CustomerRepository;
import com.app.optics.repositories.products.RecipeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RecipeRepository recipeRepository;
    private final DiscountService discountService;
    private String lastSearch = "";
    private String lastDelete = "";
    private Customer current = null;
    private Customer lastCustomer = null;
    private String notFound;

    public Customer getCustomerFromSearch() {
        String data = getLastSearch();
        if (lastCustomer != null)
            return lastCustomer;
        if (data == null || data.equals("")) {
            return new Customer();
        } else if (data.startsWith("+")) {
            return new Customer("", data);
        } else {
            return new Customer(data, "");
        }
    }

    public Customer getCurrent() {
        if (current == null) {
            return null;
        }
        List<Recipe> recipes = recipeRepository.findByCustomerId(current.getId());
        current.setProductSum(recipes.stream().map(recipe -> {
            if (recipe.getCost() == null || recipe.getCost().getTotal() == null)
                return 0;
            return recipe.getCost().getTotal();
        }).reduce(0, Integer::sum));
        current.setProductCount(recipes.size());
        current.setDiscount(discountService.getCustomerDiscount(current));
        return current;
    }

    public List<Customer> findByNameOrPhone(Customer customer) {
        return findByNameOrPhone(customer.getName(), customer.getPhone());
    }

    public List<Customer> findByNameOrPhone(String name, String phone) {
        return customerRepository.findAllByNameOrPhone(name, phone);
    }

    private String convertName(String name) {
        String str = name.toLowerCase().trim().replaceAll("ё", "е");
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public List<Customer> getCustomersBySearch(String search) {

        setLastSearch(search);

        if (search == null || search.isBlank())
            return Collections.emptyList();

        search = convertName(search);

        return findByNameOrPhone(search, search);
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void deleteCustomerById(int id) {
        customerRepository.deleteById(id);
    }

    public void createCustomer(Customer customer) {
        customer.setName(convertName(customer.getName()));
        customerRepository.save(customer);
    }

    // Current customer

    public Integer getCurrentId() {
        return isCurrent() ? current.getId() : null;
    }

    public void setCurrentId(int id) {
        this.current = getCustomerById(id);
    }

    public boolean isCurrent() {
        return current != null;
    }
}
