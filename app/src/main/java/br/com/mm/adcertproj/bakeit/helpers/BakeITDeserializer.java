package br.com.mm.adcertproj.bakeit.helpers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.mm.adcertproj.bakeit.model.Ingredient;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;

public class BakeITDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Recipe[] recipes = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(json, typeOfT);
        if(recipes != null)
            for(Recipe recipe : recipes) {
                if(recipe.getIngredients() != null)
                for(Ingredient ingredient : recipe.getIngredients()) {
                    ingredient.setRecipeId(recipe.getId());
                }
                if(recipe.getSteps() != null)
                for(Step step : recipe.getSteps()) {
                    step.setRecipeId(recipe.getId());
                }
            }
        return recipes;
    }
}
