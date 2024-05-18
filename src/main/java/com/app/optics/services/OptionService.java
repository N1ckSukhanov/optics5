package com.app.optics.services;

import com.app.optics.models.OptionType;
import com.app.optics.models.products.*;
import com.app.optics.models.products.recipe.Recipe;
import com.app.optics.repositories.OptionRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class OptionService {
    private final OptionRepository optionRepository;
    private final CustomerService customerService;
    private final DiscountService discountService;
    private Option currentOption;

    public List<String> recipeRatio;
    private List<String> recipeCoverage;
    private List<String> recipeGeometry;
    private List<String> recipeSph;
    private List<String> recipeCyl;
    private List<String> recipeAx;
    private List<String> recipeAdd;
    private List<String> recipeDist;

    private List<String> lensesName;
    private List<String> lensesColor;
    private List<String> lensesPower;
    private List<String> lensesCurvature;

    public void addProductOptions(Model model, Product product) {
        model.addAttribute("product", product);

        switch (product.getProductType()) {
            case RECIPE -> getRecipeOptions(model);
            case LENSES -> getLensesOptions(model);
            case SUNGLASSES -> model.addAttribute("isSunglasses", true);
        }
    }

    public Product createProductByType(ProductType type) {
        return switch (type) {
            case RECIPE -> new Recipe();
            case LENSES -> new Lenses();
            default -> new Other(type);
        };
    }

    public void deleteById(int id) {
        optionRepository.deleteById(id);
    }

    public List<Option> findAllByType(OptionType type) {
        return optionRepository.findAllByType(type);
    }

    public List<Option> getAllOptionTypes() {
        return Arrays.stream(OptionType.values()).map(Option::new).toList();
    }

    public void addOption(Option option) {
        if (option.getName() != null && !option.getName().isBlank())
            optionRepository.save(option);
    }

    public List<String> findNamesByOptionType(OptionType type) {
        return findAllByType(type).stream().map(Option::getName).toList();
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    static
    class RecipeOptions {
        public List<String> ratio;
        public List<String> coverage;
        public List<String> geometry;
        public List<String> sph;
        public List<String> cyl;
        public List<String> ax;
        public List<String> add;
        public List<String> dist;
    }

    public void getRecipeOptions(Model model) {
        RecipeOptions options = RecipeOptions.builder()
                .ratio(findNamesByOptionType(OptionType.RECIPE_RATIO))
                .coverage(findNamesByOptionType(OptionType.RECIPE_COVERAGE))
                .geometry(findNamesByOptionType(OptionType.RECIPE_GEOMETRY))
                .sph(getOptions(0.0, -15.0, 0.25, 15.0, 0.25, 2, true))
                .cyl(getOptions(0.0, -6.0, 0.25, 6.0, 0.25, 2, true))
                .ax(getOptions(0, 180, 1, 0, false))
                .add(getOptions(0.0, 4.0, 0.25, 2, false))
                .dist(getOptions(40, 78, 1, 0, false))
                .build();

        model.addAttribute("options", options);
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    static
    class LensesOptions {
        public List<String> name;
        public List<String> color;
        public List<String> power;
        public List<String> curvature;
    }


    public void getLensesOptions(Model model) {
        LensesOptions options = LensesOptions.builder()
                .name(findNamesByOptionType(OptionType.LENSES_NAME))
                .color(findNamesByOptionType(OptionType.LENSES_COLOR))
                .power(getOptions(0.0, -15.0, 0.25, 2, false))
                .curvature(getOptions(8.2, 9.0, 0.1, 1, false))
                .build();

        model.addAttribute("options", options);
    }

    private List<String> getOptions(double start1, double finish1, double start2, double finish2, double step, double round, boolean setSign) {
        List<String> options = getOptions(start1, finish1, step, round, setSign);
        options.add("");
        options.addAll(getOptions(start2, finish2, step, round, setSign));
        return options;
    }

    private List<String> getOptions(double start, double finish, double step, double round, boolean setSign) {
        List<String> options = new ArrayList<>();
        boolean rounding = round != 0;
        round = 10.0 * round;
        if (start > finish)
            step = -step;
        while (start != finish) {
            options.add(getRoundValue(start, setSign, rounding));
            start += step;

            start = rounding ? Math.round(start * round) / round : Math.round(start);
        }
        options.add(getRoundValue(start, setSign, rounding));
        return options;
    }

    private String getRoundValue(double start, boolean setSign, boolean rounding) {
        String sign = (start < 0.001 || !setSign) ? "" : "+";
        if (rounding)
            return sign + start;
        else
            return sign + Math.round(start);
    }
}
