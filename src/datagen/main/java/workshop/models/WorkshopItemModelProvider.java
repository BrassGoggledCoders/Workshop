package workshop.models;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;

public class WorkshopItemModelProvider extends ItemModelProvider {

    public WorkshopItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Items
        WorkshopItems.getAllItems().stream()
                .filter(item -> !WorkshopItems.BOTTLES.containsValue(item))
                .forEach(item -> this.singleTexture(item.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/" + item.getId().getPath())));
        this.singleTexture("guide", mcLoc("item/generated"), "layer0", modLoc("item/guide"));
        this.singleTexture(WorkshopBlocks.TEA_PLANT.getItem().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/tea_seeds"));
        //Fluid Items
        for (RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            bucket(fluid);
            bottle(fluid);
        }
        //BlockItems
        for (BlockRegistryObjectGroup<Block, BlockItem, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.withExistingParent(concrete.getItem().getRegistryName().getPath(), modLoc(BLOCK_FOLDER + "/" + concrete.getName()));
        }
        Lists.newArrayList(WorkshopBlocks.BELLOWS, WorkshopBlocks.ALEMBIC, WorkshopBlocks.SCRAP_BIN, WorkshopBlocks.SEASONED_LOG,
                WorkshopBlocks.SILO_BARREL, WorkshopBlocks.STRIPPED_SEASONED_LOG, WorkshopBlocks.SINTERING_FURNACE, WorkshopBlocks.DRYING_BASIN, WorkshopBlocks.FLUID_FUNNEL,
                WorkshopBlocks.PRESS, WorkshopBlocks.SILT, WorkshopBlocks.SILTSTONE, WorkshopBlocks.MORTAR, WorkshopBlocks.SEALED_BARREL,
                WorkshopBlocks.SEASONING_BARREL, WorkshopBlocks.BROKEN_ANVIL, WorkshopBlocks.MOLTEN_CHAMBER,
                WorkshopBlocks.COLLECTOR)
                .forEach(blockGroup -> this.withExistingParent(blockGroup.getName(), modLoc(String.format("%s/%s", BLOCK_FOLDER, blockGroup.getName()))));
        this.withExistingParent(WorkshopBlocks.ITEMDUCT.getName(), modLoc(BLOCK_FOLDER + "/itemduct_center"));
        this.withExistingParent(WorkshopBlocks.OBSIDIAN_PLATE.getName(), modLoc(BLOCK_FOLDER + "/obsidian_plate_up"));
    }

    private void bucket(RegistryObject<Fluid> fluid) {
        this.withExistingParent(fluid.getId().toString() + "_bucket", new ResourceLocation("forge", ITEM_FOLDER + "/bucket_drip"))
        .customLoader(DynamicBucketModelBuilder::begin)
        .fluid(fluid.get());
    }

    private void bottle(RegistryObject<Fluid> fluid) {
        this.withExistingParent(fluid.getId().toString() + "_bottle", modLoc(ITEM_FOLDER + "/bottle"))
                .customLoader(DynamicBucketModelBuilder::begin)
                .fluid(fluid.get());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Model Provider";
    }
}
