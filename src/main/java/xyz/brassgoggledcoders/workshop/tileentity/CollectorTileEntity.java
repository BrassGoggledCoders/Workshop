package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.capabilities.CollectorTarget;
import xyz.brassgoggledcoders.workshop.block.CollectorBlock;
import xyz.brassgoggledcoders.workshop.block.ItemductBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopCapabilities;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class CollectorTileEntity extends BasicMachineTileEntity<CollectorTileEntity, CollectorRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRecipeType().toString());
    private final InventoryComponent<CollectorTileEntity> output;
    public static final int outputSize = 5;

    public TileEntityType<?> type;
    public LazyOptional<CollectorTarget> capability;

    public CollectorTileEntity() {
        super(WorkshopBlocks.COLLECTOR.getTileEntityType(),
                new ProgressBarComponent<CollectorTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.VERTICAL_UP));
        int pos = 0;
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<CollectorTileEntity>(InventoryUtil.ITEM_OUTPUT, 102, 44, outputSize, pos++)
                .setColor(InventoryUtil.ITEM_OUTPUT_COLOR)
                .setInputFilter((stack, integer) -> false));
    }

    @Override
    public CollectorTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        return type != null;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRecipeType() && recipe instanceof CollectorRecipe;
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
    public ResourceLocation getRecipeCategoryUID() {
        return WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRegistryName();
    }

    @Override
    public boolean matchesInputs(CollectorRecipe currentRecipe) {
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            if(this.capability != null && currentRecipe.getTileEntityTypes().contains(type)) {
                return capability.map(target -> {
                    // Recipe is only valid if machine can accept all possible outputs from recipe
                    //&& currentRecipe.getOutputs().stream().map(rangedStack -> new ItemStack(rangedStack.stack.getItem(), rangedStack.max))
                    //                            .allMatch(itemStack -> ItemHandlerHelper.insertItemStacked(this.output, itemStack, true).isEmpty())
                    if (target.isActive()) {
                        return Stream.of(target.getCollectables()).anyMatch(stack -> currentRecipe.input.test(stack));
                    }
                    return false;
                }).orElse(false);
            }
        }
        return false;
    }

    @Override
    public void handleComplete(CollectorRecipe currentRecipe) {
        ItemHandlerHelper.insertItemStacked(this.output, currentRecipe.getRecipeOutput(this.getWorld().getRandom()), false);
        this.getMachineComponent().forceRecipeRecheck();
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        output.deserializeNBT(compound.getCompound("output"));
        super.read(state, compound);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("output", output.serializeNBT());
        return super.write(compound);
    }

    public void invalidateCache() {
        this.capability = null;
    }
}
