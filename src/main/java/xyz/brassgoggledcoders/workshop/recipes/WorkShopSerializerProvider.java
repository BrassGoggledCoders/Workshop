package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;

import java.util.Map;

public class WorkShopSerializerProvider extends TitaniumSerializableProvider {

    public WorkShopSerializerProvider(DataGenerator generatorIn, String modid) {
        super(generatorIn, modid);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> map) {
        AlembicRecipe.RECIPES.forEach(alembicRecipe -> map.put(alembicRecipe, alembicRecipe));

    }
}
