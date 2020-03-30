package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

import java.util.Map;

public class SeasoningBarrelRecipeProvider extends TitaniumSerializableProvider {
    public SeasoningBarrelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        SeasoningBarrelRecipe testRecipe = new SeasoningBarrelRecipe(new ResourceLocation(Workshop.MOD_ID, "test"), Ingredient.fromStacks(new ItemStack(Items.DIAMOND)),
                ItemStack.EMPTY, new FluidStack(WorkshopFluids.DISTILLED_WATER.get(), 100), new FluidStack(Fluids.LAVA, 100), 500);
        serializables.put(testRecipe, testRecipe);
    }
}
