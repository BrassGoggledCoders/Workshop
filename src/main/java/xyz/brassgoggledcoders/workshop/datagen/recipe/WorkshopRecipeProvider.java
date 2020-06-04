package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;

import java.util.function.Consumer;

public class WorkshopRecipeProvider extends TitaniumRecipeProvider {

    public WorkshopRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void register(Consumer<IFinishedRecipe> consumer) {
        //section Machine Self Recipes
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SEASONING_BARREL.getBlock())
                .patternLine("LSL")
                .patternLine("L L")
                .patternLine("LSL")
                .key('L', Blocks.STRIPPED_OAK_LOG)
                .key('S', Blocks.OAK_SLAB)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SPINNING_WHEEL.getBlock())
                .patternLine("W I")
                .patternLine("WWW")
                .patternLine("SSS")
                .key('S', ItemTags.WOODEN_SLABS)
                .key('W', ItemTags.PLANKS)
                .key('I', Tags.Items.INGOTS_IRON)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SINTERING_FURNACE.getBlock())
                .patternLine("BDB")
                .patternLine("G G")
                .patternLine("BFB")
                .key('B', Items.BRICKS)
                .key('G', Tags.Items.GLASS)
                .key('D', Blocks.DROPPER)
                .key('F', Blocks.FURNACE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.BELLOWS.getBlock())
                .patternLine("PPP")
                .patternLine("WWW")
                .patternLine("PPP")
                .key('P', ItemTags.PLANKS)
                .key('W', ItemTags.WOOL)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.MORTAR.getBlock())
                .patternLine("G G")
                .patternLine(" G ")
                .key('G', Blocks.GRANITE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.PRESS.getBlock())
                .patternLine("WPW")
                .patternLine("WGW")
                .patternLine("WCW")
                .key('W', ItemTags.PLANKS)
                .key('P', Items.PISTON)
                .key('G', Tags.Items.GLASS_PANES)
                .key('C', Items.COBBLESTONE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.ALEMBIC.getBlock())
                .patternLine("PPP")
                .patternLine("PPB")
                .patternLine("FSC")
                .key('F', Items.FURNACE)
                .key('P', ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "pipes/glass")))
                .key('S', Tags.Items.STONE)
                .key('C', Items.CAULDRON)
                .key('B', Items.GLASS_BOTTLE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.COLLECTOR.getBlock())
                .patternLine("C C")
                .patternLine("CCC")
                .key('C', Tags.Items.COBBLESTONE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SCRAP_BIN.getBlock())
                .patternLine("III")
                .patternLine("ICI")
                .patternLine("III")
                .key('I', ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "films/iron")))
                .key('C', Items.COMPOSTER)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.MOLTEN_CHAMBER.getBlock())
                .patternLine("COC")
                .patternLine("C C")
                .patternLine("COC")
                .key('C', WorkshopItemTagsProvider.REBARRED_CONCRETE)
                .key('O', WorkshopBlocks.OBSIDIAN_PLATE.getItem())
                .build(consumer);
        //endsection
        //section Misc
        TitaniumShapedRecipeBuilder.shapedRecipe(Items.TORCH)
                .patternLine("T")
                .patternLine("S")
                .key('T', WorkshopItems.TALLOW.get())
                .key('S', Tags.Items.RODS_WOODEN)
                .build(consumer);
        //endsection
        //TODO the builder doesn't support ItemStack outputs, only Items...
        //CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(WorkshopBlocks.BROKEN_ANVIL.getItem()), new ItemStack(Blocks.IRON_BLOCK, 3), 0.1F, 300);
    }
}
