package workshop.recipe;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tag.WorkshopItemTags;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

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
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SEASONING_BARREL.getBlock())
                .setName(new ResourceLocation(Workshop.MOD_ID, "seasoning_barrel_alt"))
                .patternLine("LSL")
                .patternLine("L L")
                .patternLine("LSL")
                .key('L', Blocks.STRIPPED_DARK_OAK_LOG)
                .key('S', Blocks.DARK_OAK_SLAB)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SEASONING_BARREL.getBlock())
                .setName(new ResourceLocation(Workshop.MOD_ID, "seasoning_barrel_seasoned"))
                .patternLine("LSL")
                .patternLine("L L")
                .patternLine("LSL")
                .key('L', WorkshopBlocks.SEASONED_LOG.get())
                .key('S', Blocks.OAK_SLAB)
                .build(consumer);
        /*TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SPINNING_WHEEL.getBlock())
                .patternLine("W I")
                .patternLine("WWW")
                .patternLine("SSS")
                .key('S', ItemTags.WOODEN_SLABS)
                .key('W', ItemTags.PLANKS)
                .key('I', Tags.Items.INGOTS_IRON)
                .build(consumer);*/
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SINTERING_FURNACE.getBlock())
                .patternLine("BDB")
                .patternLine("G G")
                .patternLine("BFB")
                .key('B', Items.BRICKS)
                .key('G', Tags.Items.GLASS)
                .key('D', Blocks.IRON_BARS)
                .key('F', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
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
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.ITEMDUCT.getBlock(), 4)
                .patternLine("WW ")
                .patternLine("  W")
                .patternLine("WW ")
                .key('W', WorkshopBlocks.SEASONED_LOG.getItem())
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.COLLECTOR.getBlock())
                .patternLine("SS ")
                .patternLine(" CS")
                .patternLine("SS ")
                .key('S', Tags.Items.STONE)
                .key('C', Tags.Items.CHESTS)
                .build(consumer);
        /*TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.PRESS.getBlock())
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

        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.MOLTEN_CHAMBER.getBlock())
                .patternLine("COC")
                .patternLine("C C")
                .patternLine("COC")
                .key('C', WorkshopItemTagsProvider.REBARRED_CONCRETE)
                .key('O', WorkshopBlocks.OBSIDIAN_PLATE.getItem())
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.FLUID_FUNNEL.getBlock())
                .patternLine("L L")
                .patternLine("LCL")
                .patternLine(" L ")
                .key('L', WorkshopBlocks.SEASONED_LOG.getItem())
                .key('C', Tags.Items.CHESTS_WOODEN)
                .build(consumer);
                */
        /*TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SCRAP_BIN.getBlock())
                .patternLine("III")
                .patternLine("ICI")
                .patternLine("III")
                .key('I', ItemTags.getCollection().get(new ResourceLocation("forge", "films/iron")))
                .key('C', Items.COMPOSTER)
                .build(consumer);*/
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.SCRAP_BIN.getBlock())
                .setName(new ResourceLocation(Workshop.MOD_ID, "scrap_bin_alt"))
                .patternLine("III")
                .patternLine("ICI")
                .patternLine("III")
                .key('I', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .key('C', Items.COMPOSTER)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(WorkshopBlocks.DRYING_BASIN.getBlock())
                .patternLine("A A")
                .patternLine("A A")
                .patternLine("AAA")
                .key('A', Blocks.POLISHED_ANDESITE)
                .build(consumer);
        /*TitaniumShapelessRecipeBuilder.shapelessRecipe(WorkshopBlocks.SILO_BARREL.getBlock())
                .addIngredient(Blocks.BARREL)
                .addIngredient(WorkshopItemTags.IRON_FILM)
                .build(consumer);*/
        //endsection        CookingRecipeBuilder.
        //section Misc
        TitaniumShapedRecipeBuilder.shapedRecipe(Items.TORCH)
                .patternLine("T")
                .patternLine("S")
                .key('T', WorkshopItemTags.TALLOW)
                .key('S', Tags.Items.RODS_WOODEN)
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ItemTags.SAPLINGS), WorkshopItems.ASH.get(), 0.05F, 20);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ItemTags.LEAVES), WorkshopItems.ASH.get(), 0.05F, 20);

        /*createThreeByThree(WorkshopResourcePlugin.COPPER_BLOCK, WorkshopResourcePlugin.COPPER_INGOT, consumer);
        createThreeByThree(WorkshopResourcePlugin.SILVER_BLOCK, WorkshopResourcePlugin.SILVER_INGOT, consumer);
        createThreeByThree(WorkshopResourcePlugin.COPPER_INGOT, WorkshopResourcePlugin.COPPER_NUGGET, consumer);
        createThreeByThree(WorkshopResourcePlugin.SILVER_INGOT, WorkshopResourcePlugin.SILVER_NUGGET, consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(WorkshopResourcePlugin.COPPER_INGOT, 9)
                .addIngredient(Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/copper"))))
                .build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(WorkshopResourcePlugin.SILVER_INGOT, 9)
                .addIngredient(Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/silver"))))
                .build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(WorkshopResourcePlugin.COPPER_NUGGET, 9)
                .addIngredient(Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/copper"))))
                .build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(WorkshopResourcePlugin.SILVER_NUGGET, 9)
                .addIngredient(Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/silver"))))
                .build(consumer);*/
        //endsection
        //TODO the builder doesn't support ItemStack outputs, only Items...
        //CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(WorkshopBlocks.BROKEN_ANVIL.getItem()), new ItemStack(Blocks.IRON_BLOCK, 3), 0.1F, 300);
    }

    /**
     * Creates a shaped recipe that converts a 3x3 grid of items into one item (usually used for ingots to blocks and nuggets to ingots)
     * @param output The item to output
     * @param input The input item, of which 9 will be required to craft the output
     * @param consumer The consumer to build the recipe with
     */
    private void createThreeByThree(IItemProvider output, Item input, Consumer<IFinishedRecipe> consumer) {
        TitaniumShapedRecipeBuilder.shapedRecipe(output)
                .patternLine("III")
                .patternLine("III")
                .patternLine("III")
                .key('I', input)
                .build(consumer, new ResourceLocation(Titanium.MODID, output.asItem().getRegistryName().getPath()) + "_from_" + input.getRegistryName().getPath());
    }
}
