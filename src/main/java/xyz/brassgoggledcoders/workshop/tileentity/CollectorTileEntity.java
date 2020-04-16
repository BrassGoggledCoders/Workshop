package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.block.CollectorBlock;
import xyz.brassgoggledcoders.workshop.capabilities.ICollectorTarget;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopCapabilities;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;

import java.util.stream.Stream;

public class CollectorTileEntity extends BasicMachineTileEntity<CollectorTileEntity, CollectorRecipe> {

    private InventoryComponent<CollectorTileEntity> output;

    public CollectorTileEntity() {
        super(WorkshopBlocks.COLLECTOR.getTileEntityType(),
                new ProgressBarComponent<CollectorTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<CollectorTileEntity>("output", 102, 44, 1, pos++)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
    }

    @Override
    public CollectorTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        return this.getWorld().getTileEntity(this.getPos().offset(this.getBlockState().get(CollectorBlock.FACING))) != null;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.COLLECTOR && recipe instanceof CollectorRecipe;
    }

    @Override
    public CollectorRecipe castRecipe(IRecipe<?> iRecipe) {
        return (CollectorRecipe) iRecipe;
    }

    @Override
    public int getProcessingTime(CollectorRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public boolean matchesInputs(CollectorRecipe currentRecipe) {
        TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(this.getBlockState().get(CollectorBlock.FACING)));
        if(tile.getType().equals(currentRecipe.targetTileType)) {
            LazyOptional<ICollectorTarget> cap = tile.getCapability(WorkshopCapabilities.COLLECTOR_TARGET);
                return cap.map(target -> {
                    if(target.isActive()) {
                        return Stream.of(target.getCollectables()).anyMatch(stack -> currentRecipe.input.test(stack));
                    }
                    return false;
                }).orElse(false);
        }
        return false;
    }

    @Override
    public void handleComplete(CollectorRecipe currentRecipe) {
        ItemHandlerHelper.insertItemStacked(this.output, currentRecipe.output, false);
        this.getMachineComponent().forceRecipeRecheck();
    }
}
