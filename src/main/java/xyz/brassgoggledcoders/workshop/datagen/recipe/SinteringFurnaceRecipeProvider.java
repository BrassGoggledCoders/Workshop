package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.BlockRegistryObjectGroup;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SinteringFurnaceRecipeProvider extends TitaniumSerializableProvider {

    public static final List<SinteringFurnaceRecipe> recipes = new ArrayList<>();

    public SinteringFurnaceRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        for(BlockRegistryObjectGroup concrete : WorkshopBlocks.CONCRETES) {
            ResourceLocation location = new ResourceLocation("minecraft", concrete.getName().replace("_rebarred_", "_"));
            recipes.add(new Builder(concrete.getName())
                    .setInput(Ingredient.fromItems(Blocks.IRON_BARS))
                    .setPowder(Ingredient.fromItems(ForgeRegistries.BLOCKS.getValue(location)))
                    .setOutput(new ItemStack(concrete.getBlock()))
                    .setTime(30 * 20)
                    .build());
        }
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private ResourceLocation name;
        private Ingredient powderIn = Ingredient.EMPTY;
        private Ingredient itemIn = Ingredient.EMPTY;
        private ItemStack itemOut = ItemStack.EMPTY;
        private int processingTime;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setPowder(Ingredient powder) {
            this.powderIn = powder;
            return this;
        }

        public Builder setInput(Ingredient in) {
            this.itemIn = in;
            return this;
        }

        public Builder setOutput(ItemStack out) {
            this.itemOut = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            if(this.processingTime <= 0) {
                throw new IllegalArgumentException("Processing time must be more than zero");
            }
        }

        public SinteringFurnaceRecipe build() {
            validate();
            return new SinteringFurnaceRecipe(name, powderIn, itemIn, itemOut, processingTime);
        }
    }
}
