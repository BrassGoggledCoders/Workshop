package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectorRecipeProvider extends TitaniumSerializableProvider {

    public static final List<CollectorRecipe> recipes = new ArrayList<>();

    public CollectorRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("test")
                    .setTarget(TileEntityType.FURNACE)
                    .setInput(Ingredient.fromItems(Items.COAL))
                    .setOutput(new ItemStack(Items.DIAMOND))
                    .build()
        );
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private ResourceLocation name;
        private TileEntityType targetTileType;
        private Ingredient input;
        private ItemStack output;
        private int processingTime = 10;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setTarget(TileEntityType type) {
            this.targetTileType = type;
            return this;
        }

        public Builder setInput(Ingredient input) {
            this.input = input;
            return this;
        }

        public Builder setOutput(ItemStack out) {
            this.output = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {

        }

        public CollectorRecipe build() {
            validate();
            return new CollectorRecipe(name, targetTileType, input, output, processingTime);
        }
    }
}
