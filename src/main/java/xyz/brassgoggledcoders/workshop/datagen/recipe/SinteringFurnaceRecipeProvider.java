package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.*;
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
            ResourceLocation location = new ResourceLocation("minecraft", concrete.getName().replace("_rebarred_", "_") + "_powder");
            recipes.add(new Builder(concrete.getName())
                    .setInput(Ingredient.fromItems(Blocks.IRON_BARS))
                    .setPowder(Ingredient.fromItems(ForgeRegistries.BLOCKS.getValue(location)))
                    .setOutput(new ItemStack(concrete.getBlock()))
                    .setTime(30 * 20)
                    .build());
        }
        recipes.add(new Builder("caramel_apple")
                    .setInput(Ingredient.fromItems(Items.APPLE))
                    .setPowder(Ingredient.fromStacks(new ItemStack(Items.SUGAR, 2)))
                    .setOutput(new ItemStack(WorkshopItems.CARAMEL_APPLE.get()))
                    .setTime(40)
                    .build()
        );
        recipes.add(new Builder("obsidian_plate")
                .setInput(Ingredient.fromStacks(new ItemStack(Items.FLINT, 4)))
                .setPowder(Ingredient.fromItems(Items.BLAZE_POWDER))
                .setTime(60 * 20)
                .build()
        );
        for (ResourceMaterial material : ResourceRegistry.getMaterials()) {
            Map<String, ForgeRegistryEntry> generated = material.getGenerated();
            if (generated.containsKey("dust")) {
                if (generated.containsKey("film")) {
                    recipes.add(new Builder(material.getMaterialType() + "_" + WorkshopResourceType.FILM.toString().toLowerCase())
                            .setPowder(Ingredient.fromTag(ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "dusts/" + material.getMaterialType()))))
                            .setInput(Ingredient.fromItems(Items.PAPER))
                            //TODO Eugh
                            .setOutput(new ItemStack(((ForgeRegistryEntry<Item>)generated.get("film")).delegate.get()))
                            .setTime(20)
                            .build());
                }
                if(generated.containsKey("pipe")) {
                    recipes.add(new Builder(material.getMaterialType() + "_" + WorkshopResourceType.PIPE.toString().toLowerCase())
                            .setPowder(Ingredient.fromTag(ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "dusts/" + material.getMaterialType()))))
                            .setInput(Ingredient.fromItems(Items.BAMBOO))
                            //TODO Eugh
                            .setOutput(new ItemStack(((ForgeRegistryEntry<Item>)generated.get("pipe")).delegate.get()))
                            .setTime(20)
                            .build());
                }
            }
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
