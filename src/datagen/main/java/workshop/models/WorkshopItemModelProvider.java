package workshop.models;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopItemModelProvider extends ItemModelProvider {

    public WorkshopItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            if (fluid.get().isSource(fluid.get().getDefaultState())) {
                bucket(fluid);
                bottle(fluid);
            }
        }
        for (BlockRegistryObjectGroup<Block, BlockItem, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.withExistingParent(concrete.getItem().getRegistryName().getPath(), modLoc(BLOCK_FOLDER + "/" + concrete.getName()));
        }

        //this.singleTexture("tallow", mcLoc("item/generated"), modLoc("items/tallow"));
        // this.singleTexture("lye", mcLoc("item/generated"), modLoc("items/lye"));
        //this.singleTexture("leather_cordage", mcLoc("item/generated"), modLoc("items/leather_cordage"));

        Lists.newArrayList(WorkshopBlocks.BELLOWS,
                WorkshopBlocks.ALEMBIC,
                WorkshopBlocks.SCRAP_BIN,
                WorkshopBlocks.SEASONED_LOG,
                WorkshopBlocks.SILO_BARREL,
                WorkshopBlocks.STRIPPED_SEASONED_LOG,
                WorkshopBlocks.SINTERING_FURNACE,
                WorkshopBlocks.DRYING_BASIN,
                WorkshopBlocks.FLUID_FUNNEL,
                WorkshopBlocks.PRESS,
                WorkshopBlocks.SILT,
                WorkshopBlocks.SILTSTONE)
                .forEach(blockGroup -> this.withExistingParent(blockGroup.getName(), modLoc(String.format("%s/%s", BLOCK_FOLDER, blockGroup.getName()))));
        this.withExistingParent(WorkshopBlocks.ITEMDUCT.getName(), modLoc(BLOCK_FOLDER + "/itemduct_center"));
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
