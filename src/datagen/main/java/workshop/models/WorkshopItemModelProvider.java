package workshop.models;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopItemModelProvider extends ModelProvider<PropertiedItemModelBuilder> {

    public WorkshopItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, ITEM_FOLDER, PropertiedItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            if (fluid.get().isSource(fluid.get().getDefaultState())) {
                bucket(fluid.getId());
                bottle(fluid.getId());
            }
        }
        for (BlockRegistryObjectGroup<Block, BlockItem, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.withExistingParent(concrete.getItem().getRegistryName().getPath(), modLoc(BLOCK_FOLDER + "/" + concrete.getName()));
        }

        //this.singleTexture("tallow", mcLoc("item/generated"), modLoc("items/tallow"));
        // this.singleTexture("lye", mcLoc("item/generated"), modLoc("items/lye"));
        //this.singleTexture("leather_cordage", mcLoc("item/generated"), modLoc("items/leather_cordage"));

        this.withExistingParent(WorkshopBlocks.BELLOWS.getName(), modLoc(BLOCK_FOLDER + "/bellows"));
        this.withExistingParent(WorkshopBlocks.ALEMBIC.getName(), modLoc(BLOCK_FOLDER + "/alembic"));
        this.withExistingParent(WorkshopBlocks.SCRAP_BIN.getName(), modLoc(BLOCK_FOLDER + "/scrap_bin"));
        this.withExistingParent(WorkshopBlocks.SEASONED_LOG.getName(), modLoc(BLOCK_FOLDER + "/seasoned_log"));
        this.withExistingParent(WorkshopBlocks.SILO_BARREL.getName(), modLoc(BLOCK_FOLDER + "/silo_barrel"));
        this.withExistingParent(WorkshopBlocks.STRIPPED_SEASONED_LOG.getName(), modLoc(BLOCK_FOLDER + "/stripped_seasoned_log"));

        //TODO Item rotations
        //this.withExistingParent(WorkshopBlocks.DRYING_BASIN.getName(), modLoc(BLOCK_FOLDER + "/drying_basin"));
        //this.withExistingParent(WorkshopBlocks.FLUID_FUNNEL.getName(), modLoc(BLOCK_FOLDER + "/fluid_funnel"));
    }

    private void bucket(ResourceLocation fluidName) {
        getBuilder(fluidName.toString() + "_bucket")
                .parent(new ModelFile.UncheckedModelFile("forge:" + ITEM_FOLDER + "/bucket_drip"))
                .property("loader", "forge:bucket")
                .property("fluid", fluidName.toString());
    }

    private void bottle(ResourceLocation fluidName) {
        getBuilder(fluidName.toString() + "_bottle")
                .parent(new ModelFile.UncheckedModelFile(modLoc(ITEM_FOLDER + "/bottle")))
                .property("loader", "forge:bucket")
                .property("fluid", fluidName.toString());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Model Provider";
    }
}
