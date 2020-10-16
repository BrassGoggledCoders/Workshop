package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.material.ResourceRegistry;
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
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourceType;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;

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
        recipes.add(new Builder("meat_to_tallow")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(WorkshopItemTagsProvider.RAW_MEAT))
                .addOutput(new ItemStack(WorkshopItems.TALLOW.get()), 1)
                .setTime(200)
                .build());
        recipes.add(new Builder("saplings_to_ash")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.BLAST_FURNACE)
                .addTarget(TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.SAPLINGS))
                .setTime(100)
//                .addOutput(new ItemStack(Items.AIR), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get()), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get(), 2), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get(), 3), 1)
                .build());
        recipes.add(new Builder("logs_to_ash")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.BLAST_FURNACE)
                .addTarget(TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.LOGS))
                .addOutput(new ItemStack(WorkshopItems.ASH.get()), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get(), 2), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get(), 3), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get(), 4), 1)
                .setTime(300)
                .build());
        recipes.add(new Builder("planks_to_ash")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.BLAST_FURNACE)
                .addTarget(TileEntityType.SMOKER)
                .setInput(Ingredient.fromTag(ItemTags.LOGS))
                .setTime(300)
//                .addOutput(new ItemStack(Items.AIR), 1)
                .addOutput(new ItemStack(WorkshopItems.ASH.get()), 1)
                .build());
        recipes.add(new Builder("gold_to_silver_and_copper")
                .addTarget(TileEntityType.FURNACE)
                .addTarget(TileEntityType.BLAST_FURNACE)
                .setInput(Ingredient.fromTag(Tags.Items.ORES_GOLD))
                .setTime(200)
                .addOutput(new ItemStack(WorkshopResourcePlugin.COPPER_NUGGET), 1)
                .addOutput(new ItemStack(WorkshopResourcePlugin.SILVER_NUGGET), 1)
                .build());
        recipes.add(new Builder("magma_to_magma_cream")
                .addTarget(WorkshopBlocks.PRESS.getTileEntityType())
                .setInput(Ingredient.fromItems(Items.MAGMA_BLOCK))
//                .addOutput(new ItemStack(Items.AIR), 1)
                .setTime(200)
                .addOutput(new ItemStack(Items.MAGMA_CREAM, 1), 1)
                .addOutput(new ItemStack(Items.MAGMA_CREAM, 2), 1)
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private CollectorRecipe.TileEntityList targetTileTypes;
        private Ingredient input;
        private List<Pair<ItemStack, Integer>> outputs;
        private int processingTime = 10;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
            this.outputs = new ArrayList<>();
            this.targetTileTypes = new CollectorRecipe.TileEntityList();
        }

        public Builder addTarget(TileEntityType type) {
            this.targetTileTypes.getTileEntityTypes().add(type);
            return this;
        }

        public Builder setInput(Ingredient input) {
            this.input = input;
            return this;
        }

        public Builder addOutput(ItemStack out, int weight) {
            this.outputs.add(Pair.of(out, weight));
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
            return new CollectorRecipe(name, targetTileTypes, input, outputs, processingTime);
        }
    }

}
