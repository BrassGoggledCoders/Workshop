package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.capabilities.CollectorTarget;
import xyz.brassgoggledcoders.workshop.block.CollectorBlock;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
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
    protected int timer = 0;
    protected final int interval = 20;

    private TileEntityType<?> type;
    private LazyOptional<CollectorTarget> capability;

    public CollectorTileEntity() {
        super(WorkshopBlocks.COLLECTOR.getTileEntityType(),
                new ProgressBarComponent<CollectorTileEntity>(82, 15, 100).setBarDirection(ProgressBarComponent.BarDirection.VERTICAL_UP));
        this.getMachineComponent().addInventory(this.output = new InventoryComponent<CollectorTileEntity>(InventoryUtil.ITEM_OUTPUT, 44, 79, outputSize)
                .setInputFilter((stack, integer) -> false));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if (timer > interval) {
                timer = 0;
                this.rebuildCache();
            }
        }
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

    public void rebuildCache() {
        Direction facing = this.getBlockState().get(CollectorBlock.FACING);
        TileEntity other = this.getWorld().getTileEntity(this.getPos().offset(facing));
        if(other != null) {
            this.type = other.getType();
            this.capability = other.getCapability(WorkshopCapabilities.COLLECTOR_TARGET, facing.getOpposite());
        }
        else {
            this.type = null;
            this.capability = null;
        }
    }
}
