package com.app.optics.models.products;

import com.app.optics.models.OptionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "options")
@NoArgsConstructor
public class Option {
    @Id
    @SequenceGenerator(name = "option_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_seq")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private OptionType type;

    private String name;

    public Option(OptionType type, String name) {
        this.type = type;
        this.name = name;
    }

    public Option(OptionType type) {
        this.type = type;
    }

    public String getReadableType() {
        return switch (type) {
            case RECIPE_RATIO -> "Рецепт на Очки - Коэффициент";
            case RECIPE_COVERAGE -> "Рецепт на Очки - Покрытие";
            case RECIPE_GEOMETRY -> "Рецепт на Очки - Геометрия";
            case LENSES_NAME -> "Контактные Линзы - Название";
            case LENSES_COLOR -> "Контактные Линзы - Цвет";
        };
    }
}

