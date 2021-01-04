package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PressRecipeProvider extends TitaniumSerializableProvider {

    public static final List<PressRecipe> recipes = new ArrayList<>();

    public PressRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("seeds_to_seed_oil")
                .setInput(Ingredient.fromTag(Tags.Items.SEEDS))
                .setOutput(new FluidStack(WorkshopFluids.SEED_OIL.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .build());
        recipes.add(new Builder("apple_juicing")
                .setInput(Ingredient.fromItems(Items.APPLE))
                .setOutput(new FluidStack(WorkshopFluids.APPLE_JUICE.getFluid(), WorkshopFluids.BOTTLE_VOLUME))
                .build());
        recipes.add(new Builder("magma_to_hellblood")
                .setInput(Ingredient.fromItems(Items.MAGMA_BLOCK))
                .setOutput(new FluidStack(WorkshopFluids.HELLBLOOD.getFluid(), FluidAttributes.BUCKET_VOLUME / 4))
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient input;
        private FluidStack output;
        private int processingTime = 6 * 20; // Six seconds in ticks

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setInput(Ingredient in) {
            this.input = in;
            return this;
        }

        public Builder setOutput(FluidStack out) {
            this.output = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            if (this.processingTime <= 0) {
                throw new IllegalArgumentException("Processing time must be more than zero");
            }
        }

        public PressRecipe build() {
            validate();
            return new PressRecipe(name, input, output);
        }
    }
}
