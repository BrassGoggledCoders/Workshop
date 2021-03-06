package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tag.WorkshopItemTags;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.DryingBasinRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DryingBasinRecipeProvider extends TitaniumSerializableProvider {

    public static final List<DryingBasinRecipe> recipes = new ArrayList<>();

    public DryingBasinRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("log_drying")
                .setItemIn(Ingredient.fromTag(ItemTags.LOGS))
                .setItemOut(new ItemStack(WorkshopBlocks.SEASONED_LOG.get()))
                .build());
        recipes.add(new Builder("stripped_log_drying")
                .setItemIn(Ingredient.fromTag(WorkshopItemTags.STRIPPED_LOGS))
                .setItemOut(new ItemStack(WorkshopBlocks.STRIPPED_SEASONED_LOG.get()))
                .build());
        recipes.add(new Builder("salt")
                .setFluidIn(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 4))
                .setItemOut(new ItemStack(WorkshopItems.SALT.get()))
                .build());
        recipes.add(new Builder("dried_kelp")
                .setItemIn(Ingredient.fromItems(Items.KELP))
                .setItemOut(new ItemStack(Items.DRIED_KELP))
                .setTime(500)
                .build());
        recipes.add(new Builder("honey")
                .setFluidIn(new FluidStack(WorkshopFluids.HONEY.get(), FluidAttributes.BUCKET_VOLUME))
                .setItemOut(new ItemStack(Blocks.HONEY_BLOCK))
                .setTime(1500)
                .build());
        recipes.add(new Builder("lye")
                .setFluidIn(new FluidStack(WorkshopFluids.POTASH_WATER.get(), FluidAttributes.BUCKET_VOLUME / 4))
                .setItemOut(new ItemStack(WorkshopItems.LYE.get()))
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient itemIn = Ingredient.EMPTY;
        private ItemStack itemOut = ItemStack.EMPTY;
        private FluidStack fluidIn = FluidStack.EMPTY;
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

        public Builder setTime(int time) {
            this.seasoningTime = time;
            return this;
        }

        public void validate() {
            if (Ingredient.EMPTY.equals(this.itemIn) && FluidStack.EMPTY.equals(this.fluidIn)) {
                throw new IllegalArgumentException("Recipe must have an input");
            }
        }

        public DryingBasinRecipe build() {
            validate();
            return new DryingBasinRecipe(name, itemIn, itemOut, fluidIn, seasoningTime);
        }
    }
}
