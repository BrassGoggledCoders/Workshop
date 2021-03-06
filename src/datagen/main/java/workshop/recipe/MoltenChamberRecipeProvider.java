package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.recipe.MoltenChamberRecipe;
import xyz.brassgoggledcoders.workshop.tag.WorkshopFluidTags;
import xyz.brassgoggledcoders.workshop.util.FluidTagInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoltenChamberRecipeProvider extends TitaniumSerializableProvider {

    public static final List<MoltenChamberRecipe> recipes = new ArrayList<>();

    public MoltenChamberRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("hellblood_to_lava")
                .setItemIn(Ingredient.fromItems(WorkshopBlocks.OBSIDIAN_PLATE.getItem()))
                .setFluidIn(new FluidTagInput(WorkshopFluidTags.getFluidTag(WorkshopFluids.HELLBLOOD), FluidAttributes.BUCKET_VOLUME))
                .setFluidOut(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME))
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient itemIn = Ingredient.EMPTY;
        private ItemStack itemOut = ItemStack.EMPTY;
        private FluidTagInput fluidIn;
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

        public Builder setFluidIn(FluidTagInput in) {
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
            if (Ingredient.EMPTY.equals(this.itemIn) && this.fluidIn == null) {
                throw new IllegalArgumentException("Recipe must have an input");
            }
        }

        public MoltenChamberRecipe build() {
            validate();
            return new MoltenChamberRecipe(name, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
        }
    }
}
