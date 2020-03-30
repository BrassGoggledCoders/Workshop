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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasoningBarrelRecipeProvider extends TitaniumSerializableProvider {

    public static final List<SeasoningBarrelRecipe> recipes = new ArrayList<>();

    public SeasoningBarrelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {

        SeasoningBarrelRecipe testRecipe = new SeasoningBarrelRecipe(new ResourceLocation(Workshop.MOD_ID, "test"), Ingredient.fromStacks(new ItemStack(Items.DIAMOND)),
                ItemStack.EMPTY, new FluidStack(WorkshopFluids.DISTILLED_WATER.get(), 100), new FluidStack(Fluids.LAVA, 100), 500);
        recipes.add(new Builder("test2")
                .setItemIn(Ingredient.fromStacks(new ItemStack(Items.ANDESITE)))
                .setFluidIn(new FluidStack(Fluids.WATER, 100))
                .setItemOut(new ItemStack(Items.DIAMOND)).build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    public static class Builder {
        private ResourceLocation name;
        private Ingredient itemIn = Ingredient.EMPTY;
        private ItemStack itemOut = ItemStack.EMPTY;
        private FluidStack fluidIn = FluidStack.EMPTY;
        private FluidStack fluidOut = FluidStack.EMPTY;
        private int seasoningTime = 1000;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setItemIn(Ingredient in) {
            this.itemIn = in;
            return this;
        }

        public Builder setItemOut(ItemStack out) {
            this.itemOut = out;
            return this;
        }

        public Builder setFluidIn(FluidStack in) {
            this.fluidIn = in;
            return this;
        }

        public Builder setFluidOut(FluidStack out) {
            this.fluidOut = out;
            return this;
        }

        public Builder setTime(int time) {
            this.seasoningTime = time;
            return this;
        }

        public SeasoningBarrelRecipe build() {
            //validate()
            return new SeasoningBarrelRecipe(name, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
        }
    }
}