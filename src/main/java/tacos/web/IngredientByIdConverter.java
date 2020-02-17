package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.data.IngredientRepository;
import tacos.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private IngredientRepository ingredientRepo;

    @Autowired
    public IngredientByIdConverter ( IngredientRepository ingredientRepo){
        this.ingredientRepo = ingredientRepo;
    }

    @Override
    public Ingredient convert(String source) {
        System.out.println("TRYING TO CONVERT....."+ source);

        List<Ingredient> ingredients = new ArrayList<>();

        ingredientRepo.findAll().forEach(ingredients::add);

        for (Ingredient ingredient : ingredients) {
            // You may use equal() method
            if (ingredient.getId().equals(source))
                return ingredient;
        }
        return null;
    }
}
