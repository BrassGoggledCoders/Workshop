package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.WorkshopItemTags;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SeasoningBarrelRecipeProvider extends TitaniumSerializableProvider {

    public static final List<SeasoningBarrelRecipe> recipes = new ArrayList<>();

    public SeasoningBarrelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("water_to_brine")
                .setItemIn(Ingredient.fromTag(WorkshopItemTags.SALT))
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
        Stream.of(Pair.of(Items.TUBE_CORAL_FAN, Items.TUBE_CORAL_BLOCK), Pair.of(Items.BRAIN_CORAL_FAN, Items.BRAIN_CORAL_BLOCK)
                , Pair.of(Items.BUBBLE_CORAL_FAN, Items.BUBBLE_CORAL_BLOCK), Pair.of(Items.FIRE_CORAL_FAN, Items.FIRE_CORAL_BLOCK),
                Pair.of(Items.HORN_CORAL_FAN, Items.HORN_CORAL_BLOCK)).forEach(pair ->
                recipes.add(new Builder(pair.getSecond().getRegistryName().getPath())
                        .setItemIn(Ingredient.fromItems(pair.getFirst()))
                        .setFluidIn(new FluidStack(WorkshopFluids.BRINE.get(), WorkshopFluids.BOTTLE_VOLUME))
                        .setItemOut(new ItemStack(pair.getSecond()))
                        .setTime(20 * 60 * 5)
                        .build()));
        recipes.add(new Builder("mead")
                .setFluidIn(new FluidStack(WorkshopFluids.HONEY.get(), WorkshopFluids.BOTTLE_VOLUME))
                .setFluidOut(new FluidStack(WorkshopFluids.MEAD.get(), WorkshopFluids.BOTTLE_VOLUME))
                .setTime(20 * 60 * 3)
                .build());
        recipes.add(new Builder("potash_water")
                .setItemIn(Ingredient.fromItems(WorkshopItems.ASH.get()))
                .setFluidIn(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 4))
                .setFluidOut(new FluidStack(WorkshopFluids.POTASH_WATER.get(), FluidAttributes.BUCKET_VOLUME / 4))
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
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
            if (Ingredient.EMPTY.equals(this.itemIn) && FluidStack.EMPTY.equals(this.fluidIn)) {
                throw new IllegalArgumentException("Seasoning barrel recipe must have an input");
            }
        }

        public SeasoningBarrelRecipe build() {
            validate();
            return new SeasoningBarrelRecipe(name, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
        }
    }
}