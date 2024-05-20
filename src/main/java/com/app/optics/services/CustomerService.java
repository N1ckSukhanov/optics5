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

    public static String convertCyrilic(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'ѓ', 'е', 'ж', 'з', 'ѕ', 'и', 'ј', 'к', 'л', 'љ', 'м', 'н', 'њ', 'о', 'п', 'р', 'с', 'т', 'ќ', 'у', 'ф', 'х', 'ц', 'ч', 'џ', 'ш', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Ј', 'К', 'Л', 'Љ', 'М', 'Н', 'Њ', 'О', 'П', 'Р', 'С', 'Т', 'Ќ', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Џ', 'Ш', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "]", "e", "zh", "z", "y", "i", "j", "k", "l", "q", "m", "n", "w", "o", "p", "r", "s", "t", "'", "u", "f", "h", "c", ";", "x", "{", "A", "B", "V", "G", "D", "}", "E", "Zh", "Z", "Y", "I", "J", "K", "L", "Q", "M", "N", "W", "O", "P", "R", "S", "T", "KJ", "U", "F", "H", "C", ":", "X", "{", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "/", "-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
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
