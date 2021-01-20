package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tag.WorkshopItemTags;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CollectorRecipeProvider extends TitaniumSerializableProvider {

    public static final List<CollectorRecipe> recipes = new ArrayList<>();

    public CollectorRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("meat_to_tallow")
                .addTarget(TileEntityType.FURNACE, TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(WorkshopItemTags.RAW_MEAT))
                .addOutput(new ItemStack(WorkshopItems.TALLOW.get()))
                .setTime(200)
                .build());
        recipes.add(new Builder("saplings_to_ash")
                .addTarget(TileEntityType.FURNACE, TileEntityType.BLAST_FURNACE, TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.SAPLINGS))
                .setTime(100)
                .addOutput(new RangedItemStack(WorkshopItems.ASH.get(), 0, 3))
                .build());
        recipes.add(new Builder("logs_to_ash")
                .addTarget(TileEntityType.FURNACE, TileEntityType.BLAST_FURNACE, TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.LOGS))
                .addOutput(new RangedItemStack(WorkshopItems.ASH.get(), 1, 4))
                .setTime(600)
                .build());
        recipes.add(new Builder("planks_to_ash")
                .addTarget(TileEntityType.FURNACE, TileEntityType.BLAST_FURNACE, TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.LOGS))
                .setTime(300)
                .addOutput(new RangedItemStack(WorkshopItems.ASH.get(), 0, 1))
                .build());
        /*recipes.add(new Builder("gold_to_silver_and_copper")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.BLAST_FURNACE)
                .setInput(Ingredient.fromTag(Tags.Items.ORES_GOLD))
                .setTime(200)
                .addOutput(new ItemStack(WorkshopResourcePlugin.COPPER_NUGGET), 1)
                .addOutput(new ItemStack(WorkshopResourcePlugin.SILVER_NUGGET), 1)
                .build());*/
        recipes.add(new Builder("magma_to_magma_cream")
                .addTarget(WorkshopBlocks.PRESS.getTileEntityType())
                .setInput(Ingredient.fromItems(Items.MAGMA_BLOCK))
                .setTime(200)
                .addOutput(new RangedItemStack(Items.MAGMA_CREAM, 0, 3))
                .build());
        recipes.add(new Builder("honeycomb")
                .addTarget(TileEntityType.BEEHIVE)
                .setInput(Ingredient.fromItems(Items.BEDROCK))
                .setTime(6000)
                .addOutput(new RangedItemStack(Items.HONEYCOMB, 1, 2))
            .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private final ArrayList<TileEntityType<?>> targetTileTypes = new ArrayList<>();
        private Ingredient input;
        private final ArrayList<RangedItemStack> outputs = new ArrayList<>();
        private int processingTime = 10;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder addTarget(TileEntityType<?>... type) {
            targetTileTypes.addAll(Arrays.asList(type));
            return this;
        }

        public Builder setInput(Ingredient input) {
            this.input = input;
            return this;
        }

        public Builder addOutput(ItemStack stack) {
            this.outputs.add(new RangedItemStack(stack));
            return this;
        }

        public Builder addOutput(RangedItemStack stack) {
            this.outputs.add(stack);
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            if(this.outputs.isEmpty()) {
                throw new IllegalArgumentException("Recipe must have an output");
            }
            if(this.targetTileTypes.isEmpty()) {
                throw new IllegalArgumentException("Recipe must have target tile entities");
            }
        }

        public CollectorRecipe build() {
            validate();
            return new CollectorRecipe(name, input, processingTime,
                    outputs.toArray(new RangedItemStack[outputs.size()]), targetTileTypes.toArray(new TileEntityType[targetTileTypes.size()]));
        }
    }

}
