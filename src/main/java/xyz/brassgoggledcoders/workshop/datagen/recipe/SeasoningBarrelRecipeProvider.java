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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

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
        recipes.add(new Builder("water_to_brine")
                .setItemIn(Ingredient.fromItems(WorkshopItems.SALT.get()))
                .setFluidIn(new FluidStack(Fluids.WATER, 100))
                .setFluidOut(new FluidStack(WorkshopFluids.BRINE.getFluid(), 100))
                .build());
        recipes.add(new Builder("seed_oil_to_resin")
                .setFluidIn(new FluidStack(WorkshopFluids.SEED_OIL.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setFluidOut(new FluidStack(WorkshopFluids.RESIN.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setItemOut(new ItemStack(WorkshopItems.ROSIN.get()))
                .setTime(10 * 20)
                .build());
        recipes.add(new Builder("pickles")
                .setFluidIn(new FluidStack(WorkshopFluids.BRINE.getFluid(), FluidAttributes.BUCKET_VOLUME / 10))
                .setItemIn(Ingredient.fromItems(Items.SEA_PICKLE))
                .setItemOut(new ItemStack(WorkshopItems.PICKLE.get()))
                .setFluidOut(new FluidStack(WorkshopFluids.BRINE.getFluid(), FluidAttributes.BUCKET_VOLUME / 10))
                .setTime(15 * 20)
                .build());
        recipes.add(new Builder("apple_juice_to_cider")
                .setFluidIn(new FluidStack(WorkshopFluids.APPLE_JUICE.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setFluidOut(new FluidStack(WorkshopFluids.CIDER.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setTime(5 * 60 * 20)
                .build());
        recipes.add(new Builder("tea")
                .setFluidIn(new FluidStack(Fluids.WATER, WorkshopFluids.BOTTLE_VOLUME))
                .setFluidOut(new FluidStack(WorkshopFluids.TEA.getFluid(), WorkshopFluids.BOTTLE_VOLUME))
                .setItemIn(Ingredient.fromItems(WorkshopItems.TEA_LEAVES.get()))
                .setTime(60 * 20)
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
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

        public void validate() {
            if(Ingredient.EMPTY.equals(this.itemIn) && FluidStack.EMPTY.equals(this.fluidIn)) {
                throw new IllegalArgumentException("Seasoning barrel recipe must have an input");
            }
        }

        public SeasoningBarrelRecipe build() {
            validate();
            return new SeasoningBarrelRecipe(name, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
        }
    }
}
