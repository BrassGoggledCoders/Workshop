package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;

import java.util.Map;

public class WorkshopSerializerProvider extends TitaniumSerializableProvider {

    public WorkshopSerializerProvider(DataGenerator generatorIn, String modid) {
        super(generatorIn, modid);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> map) {
        AlembicRecipe.RECIPES.forEach(alembicRecipe -> map.put(alembicRecipe, alembicRecipe));
        SpinningWheelRecipe.RECIPES.forEach(spinningWheelRecipe -> map.put(spinningWheelRecipe, spinningWheelRecipe));
        SeasoningBarrelRecipe.RECIPES.forEach(seasoningBarrelRecipe -> map.put(seasoningBarrelRecipe, seasoningBarrelRecipe));
        SinteringFurnaceRecipe.RECIPES.forEach(sinteringFurnaceRecipe -> map.put(sinteringFurnaceRecipe, sinteringFurnaceRecipe));
        PressRecipes.RECIPES.forEach(pressRecipes -> map.put(pressRecipes, pressRecipes));

    }
}
